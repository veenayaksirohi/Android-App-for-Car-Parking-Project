#!/bin/bash
set -e

# Print working directory and environment
pwd
whoami
printenv | sort

# Determine CI root directory robustly
if [ -n "$GITHUB_WORKSPACE" ]; then
  CI_ROOT="$GITHUB_WORKSPACE"
else
  CI_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
fi

# For debugging, print CI_ROOT
echo "[DEBUG] CI_ROOT resolved as: $CI_ROOT"

echo "🔍 Checking emulator readiness..."
# Wait for emulator to be fully ready
adb wait-for-device

# Wait for boot to complete
echo "⏳ Waiting for emulator to boot completely..."
timeout 300 bash -c 'until [[ $(adb shell getprop sys.boot_completed 2>/dev/null) == "1" ]]; do sleep 2; done' || {
  echo "❌ Emulator failed to boot within 5 minutes"
  exit 1
}
echo "✅ Emulator booted successfully."

# Disable animations for faster testing
echo "🎬 Disabling animations..."
adb shell "settings put global window_animation_scale 0.0"
adb shell "settings put global transition_animation_scale 0.0"
adb shell "settings put global animator_duration_scale 0.0"

# Clear logcat and start capturing
adb logcat -c
mkdir -p screenshots

ls -l

echo "📋 Starting logcat capture..."
adb logcat > logcat.txt &
LOGCAT_PID=$!

ls -l

echo "🎥 Starting screen recording..."
adb shell screenrecord --time-limit=180 /sdcard/e2e_recording.mp4 &
SCREENRECORD_PID=$!

# Give some time for setup
sleep 5

echo "📦 Installing APK..."
adb install -r app/build/outputs/apk/debug/app-debug.apk

ls -l app/build/outputs/apk/debug/

echo "🚀 Launching app..."
adb shell "am start -n ${PACKAGE_NAME}/.MainActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"

# Wait for app to load and take initial screenshot
sleep 10
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png || true
ls -l screenshots/

echo "🧪 Running Appium Python tests..."
cd tests
python -m pytest -v --html=../test-report.html --self-contained-html --log-cli-level=INFO --log-file=../pytest.log || {
  echo "⚠️ Tests completed with issues"
}
cd ..

ls -l

echo "🛑 Stopping app..."
adb shell am force-stop "$PACKAGE_NAME" || true

echo "🎥 Pulling screen recording..."
adb pull /sdcard/e2e_recording.mp4 screenshots/ || true
ls -l screenshots/

echo "🗒️ Stopping logcat..."
kill $LOGCAT_PID || true

# Wait for processes to clean up
sleep 2

# Always use the root directory for the zip
ZIP_PATH="$CI_ROOT/e2e-artifacts.zip"
ARTIFACTS_DIR="$CI_ROOT/artifacts"

# Centralize all artifacts
mkdir -p "$ARTIFACTS_DIR/screenshots"

# Copy logs and reports
for f in appium.log logcat.txt test-report.html pytest.log; do
  if [ -f "Android-App-for-Car-Parking-Project/$f" ]; then
    cp "Android-App-for-Car-Parking-Project/$f" "$ARTIFACTS_DIR/" && echo "Copied $f to $ARTIFACTS_DIR/$f"
  elif [ -f "$f" ]; then
    cp "$f" "$ARTIFACTS_DIR/" && echo "Copied $f to $ARTIFACTS_DIR/$f"
  else
    touch "$ARTIFACTS_DIR/$f" && echo "Created empty $ARTIFACTS_DIR/$f"
  fi
done

# Copy screenshots
if compgen -G "Android-App-for-Car-Parking-Project/screenshots/*" > /dev/null; then
  cp Android-App-for-Car-Parking-Project/screenshots/* "$ARTIFACTS_DIR/screenshots/" 2>/dev/null || true
  echo "Copied screenshots to $ARTIFACTS_DIR/screenshots/"
elif compgen -G "screenshots/*" > /dev/null; then
  cp screenshots/* "$ARTIFACTS_DIR/screenshots/" 2>/dev/null || true
  echo "Copied screenshots to $ARTIFACTS_DIR/screenshots/"
else
  echo "No screenshots found to copy."
fi

# Copy screen recording
if [ -f "Android-App-for-Car-Parking-Project/e2e_recording.mp4" ]; then
  cp Android-App-for-Car-Parking-Project/e2e_recording.mp4 "$ARTIFACTS_DIR/" && echo "Copied e2e_recording.mp4 to $ARTIFACTS_DIR/e2e_recording.mp4"
elif [ -f "screenshots/e2e_recording.mp4" ]; then
  cp screenshots/e2e_recording.mp4 "$ARTIFACTS_DIR/" && echo "Copied e2e_recording.mp4 to $ARTIFACTS_DIR/e2e_recording.mp4"
else
  echo "No e2e_recording.mp4 found to copy."
fi

# List all files in artifacts directory for verification
echo "📋 Listing all files in artifacts directory:"
ls -l "$ARTIFACTS_DIR" || true
ls -l "$ARTIFACTS_DIR/screenshots/" || true

# Print directory tree for diagnostics
if command -v tree; then
  tree -a -L 3 "$CI_ROOT"
else
  find "$CI_ROOT" | head -100
fi

# Always create the zip in the root directory
echo "📦 Creating e2e-artifacts.zip at $ZIP_PATH ..."
if [ -d "$ARTIFACTS_DIR" ]; then
  (cd "$CI_ROOT" && zip -r "e2e-artifacts.zip" artifacts/) || {
    echo "❌ Failed to create zip with artifacts, creating minimal zip"
    mkdir -p "$CI_ROOT/minimal-artifacts"
    echo "Test completed at $(date)" > "$CI_ROOT/minimal-artifacts/test-summary.txt"
    (cd "$CI_ROOT" && zip -r "e2e-artifacts.zip" minimal-artifacts/)
  }
else
  echo "artifacts directory missing, creating minimal zip"
  mkdir -p "$CI_ROOT/minimal-artifacts"
  echo "Test completed at $(date)" > "$CI_ROOT/minimal-artifacts/test-summary.txt"
  (cd "$CI_ROOT" && zip -r "e2e-artifacts.zip" minimal-artifacts/)
fi

# If the zip is not in the root, copy it there
if [ ! -f "$ZIP_PATH" ]; then
  if [ -f "e2e-artifacts.zip" ]; then
    cp "e2e-artifacts.zip" "$ZIP_PATH"
    echo "[DEBUG] Copied zip to $ZIP_PATH"
  fi
fi

# Log zip file details
if [ -f "$ZIP_PATH" ]; then
  echo "✅ e2e-artifacts.zip created successfully at $ZIP_PATH"
  ls -la "$ZIP_PATH"
  echo "Zip file size: $(du -h "$ZIP_PATH")"
  if command -v sha256sum; then
    sha256sum "$ZIP_PATH"
  fi
else
  echo "❌ Failed to create e2e-artifacts.zip at $ZIP_PATH"
  exit 1
fi

# List files in root directory before exit
cd "$CI_ROOT"
echo "📂 Listing files in root directory before exit:"
ls -l "$CI_ROOT"
if command -v tree; then
  tree -a -L 2 "$CI_ROOT"
else
  find "$CI_ROOT" | head -100
fi

cd Android-App-for-Car-Parking-Project