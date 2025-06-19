# Run pytest and capture both HTML report and logs
mkdir -p screenshots
python -m pytest -v --html=./test-report.html --self-contained-html --log-cli-level=INFO --log-file=pytest.log || {
  echo "⚠️ Tests completed with issues"
}

# After launching the app, take a screenshot
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png

# ... rest of your test steps ...

# At the end, zip all artifacts (from inside Android-App-for-Car-Parking-Project)
echo "🎉 E2E test script completed."

# Only zip files that exist to avoid errors
zip -r e2e-artifacts.zip test-report.html pytest.log appium.log logcat.txt screenshots e2e_recording.mp4 2>/dev/null || echo "Some files may be missing, but continuing"