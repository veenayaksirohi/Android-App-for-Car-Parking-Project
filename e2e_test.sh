# Run pytest and capture both HTML report and logs
mkdir -p screenshots
python -m pytest -v --html=./test-report.html --self-contained-html --log-cli-level=INFO --log-file=../pytest.log || {
  echo "⚠️ Tests completed with issues"
}

# After launching the app, take a screenshot
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png

# ... rest of your test steps ...

# At the end, move/copy all artifacts to a root-level artifacts directory
ARTIFACTS_DIR="$GITHUB_WORKSPACE/artifacts"
mkdir -p "$ARTIFACTS_DIR"

cp -f test-report.html "$ARTIFACTS_DIR/" || true
cp -f ../pytest.log "$ARTIFACTS_DIR/" || true
cp -f ../appium.log "$ARTIFACTS_DIR/" || true
cp -f logcat.txt "$ARTIFACTS_DIR/" || true
cp -f screenshots/e2e_recording.mp4 "$ARTIFACTS_DIR/" || true
cp -rf screenshots "$ARTIFACTS_DIR/" || true

# Ensure logs are flushed
sleep 2
sync

# Preview logs
cat "$ARTIFACTS_DIR/appium.log" || true
cat "$ARTIFACTS_DIR/pytest.log" || true

# List all files in artifacts directory for verification
ls -l "$ARTIFACTS_DIR" || true

# Zip everything in artifacts directory (from workflow root)
cd "$GITHUB_WORKSPACE"
zip -r e2e-artifacts.zip artifacts || echo "Some files may be missing, but continuing"
cd "$GITHUB_WORKSPACE"

# Ensure the zip exists for CI artifact upload
if [ ! -f e2e-artifacts.zip ]; then
  echo "Creating empty artifact zip to ensure upload step always succeeds."
  zip -r e2e-artifacts.zip .gitkeep || true
fi