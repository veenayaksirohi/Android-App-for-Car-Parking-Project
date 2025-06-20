#!/bin/bash
set -e

# Print working directory and environment
pwd
whoami
printenv | sort

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

# Centralize all artifacts
ROOT_DIR=$(pwd)
echo "🗂️ Centralizing all artifacts in root-level artifacts directory at: $ROOT_DIR/artifacts"
mkdir -p artifacts/screenshots

# Copy logs and reports
for f in appium.log logcat.txt test-report.html pytest.log; do
  if [ -f "Android-App-for-Car-Parking-Project/$f" ]; then
    cp "Android-App-for-Car-Parking-Project/$f" artifacts/ && echo "Copied $f to $ROOT_DIR/artifacts/$f"
  elif [ -f "$f" ]; then
    cp "$f" artifacts/ && echo "Copied $f to $ROOT_DIR/artifacts/$f"
  else
    touch "artifacts/$f" && echo "Created empty $ROOT_DIR/artifacts/$f"
  fi
done

# Copy screenshots
if compgen -G "Android-App-for-Car-Parking-Project/screenshots/*" > /dev/null; then
  cp Android-App-for-Car-Parking-Project/screenshots/* artifacts/screenshots/ 2>/dev/null || true
  echo "Copied screenshots to $ROOT_DIR/artifacts/screenshots/"
elif compgen -G "screenshots/*" > /dev/null; then
  cp screenshots/* artifacts/screenshots/ 2>/dev/null || true
  echo "Copied screenshots to $ROOT_DIR/artifacts/screenshots/"
else
  echo "No screenshots found to copy."
fi

# Copy screen recording
if [ -f "Android-App-for-Car-Parking-Project/e2e_recording.mp4" ]; then
  cp Android-App-for-Car-Parking-Project/e2e_recording.mp4 artifacts/ && echo "Copied e2e_recording.mp4 to $ROOT_DIR/artifacts/e2e_recording.mp4"
elif [ -f "screenshots/e2e_recording.mp4" ]; then
  cp screenshots/e2e_recording.mp4 artifacts/ && echo "Copied e2e_recording.mp4 to $ROOT_DIR/artifacts/e2e_recording.mp4"
else
  echo "No e2e_recording.mp4 found to copy."
fi

# List all files in artifacts directory for verification
echo "📋 Listing all files in artifacts directory:"
ls -l artifacts/ || true
ls -l artifacts/screenshots/ || true

# Print directory tree for diagnostics
if command -v tree; then
  tree -a -L 3
else
  find . | head -100
fi

# Always create the zip in the root directory
echo "📦 Creating e2e-artifacts.zip in root directory..."
if [ -d artifacts ]; then
  zip -r e2e-artifacts.zip artifacts/ || {
    echo "❌ Failed to create zip with artifacts, creating minimal zip"
    mkdir -p minimal-artifacts
    echo "Test completed at $(date)" > minimal-artifacts/test-summary.txt
    zip -r e2e-artifacts.zip minimal-artifacts/
  }
else
  echo "artifacts directory missing, creating minimal zip"
  mkdir -p minimal-artifacts
  echo "Test completed at $(date)" > minimal-artifacts/test-summary.txt
  zip -r e2e-artifacts.zip minimal-artifacts/
fi

# Log zip file details
echo "🔍 Verifying the zip file exists..."
if [ -f "e2e-artifacts.zip" ]; then
  echo "✅ e2e-artifacts.zip created successfully"
  ls -la e2e-artifacts.zip
  echo "Zip file size: $(du -h e2e-artifacts.zip)"
  if command -v sha256sum; then
    sha256sum e2e-artifacts.zip
  fi
else
  echo "❌ Failed to create e2e-artifacts.zip"
  exit 1
fi

# List files in root directory before exit
echo "📂 Listing files in root directory before exit:"
ls -l
if command -v tree; then
  tree -a -L 2
else
  find . | head -100
fi

cd Android-App-for-Car-Parking-Project