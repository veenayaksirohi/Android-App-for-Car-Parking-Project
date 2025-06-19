# Run pytest and capture both HTML report and logs
python -m pytest -v --html=./test-report.html --self-contained-html | tee pytest.log || {
  echo "⚠️ Tests completed with issues"
}

# After launching the app, take a screenshot
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png

# ... rest of your test steps ...

# At the end, zip all artifacts (from inside Android-App-for-Car-Parking-Project)
echo "🎉 E2E test script completed."

# Zip all artifacts for CI upload
zip -r e2e-artifacts.zip test-report.html pytest.log appium.log logcat.txt screenshots e2e_recording.mp4 || echo "Some files may be missing, but continuing"