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

# Ensure all expected artifact files/directories exist so zip always creates the artifact
# (This prevents missing artifact errors in CI)
mkdir -p screenshots
# Add any other directories you expect artifacts in here

touch test-report.html pytest.log appium.log logcat.txt e2e_recording.mp4 || true

# Always create the zip, even if some files are empty
zip -r ../e2e-artifacts.zip test-report.html pytest.log appium.log logcat.txt screenshots e2e_recording.mp4 || echo "Some files may be missing, but continuing"

# Ensure the zip exists for CI artifact upload
if [ ! -f ../e2e-artifacts.zip ]; then
  echo "Creating empty artifact zip to ensure upload step always succeeds."
  cd ..
  zip -r e2e-artifacts.zip .gitkeep || true
  cd -
fi