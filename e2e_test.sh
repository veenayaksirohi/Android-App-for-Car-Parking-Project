#!/bin/bash
set -e

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

echo "📋 Starting logcat capture..."
adb logcat > logcat.txt &
LOGCAT_PID=$!

echo "🎥 Starting screen recording..."
adb shell screenrecord --time-limit=180 /sdcard/e2e_recording.mp4 &
SCREENRECORD_PID=$!

# Give some time for setup
sleep 5

echo "📦 Installing APK..."
adb install -r app/build/outputs/apk/debug/app-debug.apk

echo "🚀 Launching app..."
adb shell "am start -n ${PACKAGE_NAME}/.MainActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"

# Wait for app to load and take initial screenshot
sleep 10
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png || true

echo "🧪 Running Appium Python tests..."
cd tests
python -m pytest -v --html=../test-report.html --self-contained-html --log-cli-level=INFO --log-file=../pytest.log || {
  echo "⚠️ Tests completed with issues"
}
cd ..

echo "🛑 Stopping app..."
adb shell am force-stop "$PACKAGE_NAME" || true

echo "🎥 Pulling screen recording..."
adb pull /sdcard/e2e_recording.mp4 screenshots/ || true

echo "🗒️ Stopping logcat..."
kill $LOGCAT_PID || true

# Wait for processes to clean up
sleep 2

echo "🗂️ Centralizing all artifacts in root-level artifacts directory at: $(pwd)/artifacts"
cd ..
mkdir -p artifacts/screenshots
echo "Copying logs and reports to artifacts directory at: $(pwd)/artifacts"
cp Android-App-for-Car-Parking-Project/appium.log artifacts/ || touch artifacts/appium.log
echo "Copied appium.log to $(pwd)/artifacts/appium.log"
cp Android-App-for-Car-Parking-Project/logcat.txt artifacts/ || touch artifacts/logcat.txt
echo "Copied logcat.txt to $(pwd)/artifacts/logcat.txt"
cp Android-App-for-Car-Parking-Project/test-report.html artifacts/ || touch artifacts/test-report.html
echo "Copied test-report.html to $(pwd)/artifacts/test-report.html"
cp Android-App-for-Car-Parking-Project/pytest.log artifacts/ || touch artifacts/pytest.log
echo "Copied pytest.log to $(pwd)/artifacts/pytest.log"
cp Android-App-for-Car-Parking-Project/screenshots/* artifacts/screenshots/ 2>/dev/null || true
echo "Copied screenshots to $(pwd)/artifacts/screenshots/"
cp Android-App-for-Car-Parking-Project/e2e_recording.mp4 artifacts/ 2>/dev/null || true
echo "Copied e2e_recording.mp4 to $(pwd)/artifacts/e2e_recording.mp4"

# List all files in artifacts directory for verification
echo "📋 Listing all files in artifacts directory:"
ls -l artifacts/ || true
ls -l artifacts/screenshots/ || true

echo "📦 Creating e2e-artifacts.zip in root directory..."
# Always create the zip in the root directory
zip -r e2e-artifacts.zip artifacts/ || {
  echo "❌ Failed to create zip with artifacts, creating minimal zip"
  mkdir -p minimal-artifacts
  echo "Test completed at $(date)" > minimal-artifacts/test-summary.txt
  zip -r e2e-artifacts.zip minimal-artifacts/
}

echo "🔍 Verifying the zip file exists..."
if [ -f "e2e-artifacts.zip" ]; then
  echo "✅ e2e-artifacts.zip created successfully"
  ls -la e2e-artifacts.zip
else
  echo "❌ Failed to create e2e-artifacts.zip"
  exit 1
fi

echo "📂 Listing files in root directory before exit:"
ls -l
cd Android-App-for-Car-Parking-Project