# e2e_test.sh (in Android-App-for-Car-Parking-Project)
#!/bin/bash
set -e

echo "🔍 Checking emulator readiness..."
adb wait-for-device

echo "⏳ Waiting for emulator boot completion..."
# Wait up to 5 min
timeout 300 bash -c \
  'until adb shell getprop sys.boot_completed 2>/dev/null | grep -q 1; do sleep 2; done' || {
    echo "❌ Emulator failed to boot"; exit 1;
}
echo "✅ Emulator is up."

echo "🎬 Disabling animations..."
adb shell settings put global window_animation_scale 0.0
adb shell settings put global transition_animation_scale 0.0
adb shell settings put global animator_duration_scale 0.0

echo "📋 Starting logcat..."
adb logcat -c
mkdir -p screenshots
adb logcat > logcat.txt & LOGCAT_PID=$!

echo "🎥 Starting screen recording..."
adb shell screenrecord --time-limit=180 /sdcard/e2e_recording.mp4 & SCREEN_PID=$!

sleep 5
echo "📦 Installing APK..."
adb install -r app/build/outputs/apk/debug/app-debug.apk

echo "🚀 Launching app..."
adb shell am start -n "${PACKAGE_NAME}/.MainActivity" \
  -a android.intent.action.MAIN \
  -c android.intent.category.LAUNCHER

sleep 10
echo "🧪 Running Appium tests via pytest..."
cd tests
python -m pytest -v --html=../test-report.html --self-contained-html || \
  echo "⚠️ Some tests failed"
cd ..

echo "🛑 Stopping app..."
adb shell am force-stop "$PACKAGE_NAME" || true

echo "🎥 Pulling screen recording..."
adb pull /sdcard/e2e_recording.mp4 screenshots/ || true

echo "🗒️ Stopping logcat..."
kill $LOGCAT_PID || true

echo "📂 Collected artifacts:"
ls -la screenshots/ test-report.html || true
