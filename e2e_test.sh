# Run pytest and capture both HTML report and logs
mkdir -p screenshots
python -m pytest -v --html=./test-report.html --self-contained-html --log-cli-level=INFO --log-file=../pytest.log || {
  echo "⚠️ Tests completed with issues"
}

# After launching the app, take a screenshot
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png

# ... rest of your test steps ...

# Define the absolute path to the root-level artifacts directory
ARTIFACTS_DIR="$(pwd)/../artifacts"
mkdir -p "$ARTIFACTS_DIR"

# Copy all relevant artifacts to the root-level artifacts directory
cp -f test-report.html "$ARTIFACTS_DIR/" || true
cp -f pytest.log "$ARTIFACTS_DIR/" || true
cp -f appium.log "$ARTIFACTS_DIR/" || true
cp -f logcat.txt "$ARTIFACTS_DIR/" || true
cp -f e2e_recording.mp4 "$ARTIFACTS_DIR/" || true
cp -rf screenshots "$ARTIFACTS_DIR/" || true

# Ensure logs are flushed
sleep 2
sync

# List all files in artifacts directory for verification
ls -l "$ARTIFACTS_DIR" || true

# Zip everything in artifacts directory at the root
cd "$ARTIFACTS_DIR"
zip -r ../../e2e-artifacts.zip . || echo "Some files may be missing, but continuing"
cd -

# Ensure the zip exists for CI artifact upload
if [ ! -f e2e-artifacts.zip ]; then
  echo "Creating empty artifact zip to ensure upload step always succeeds."
  zip -r e2e-artifacts.zip .gitkeep || true
fi