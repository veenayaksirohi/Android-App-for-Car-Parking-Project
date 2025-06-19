-          python -m pytest -v --html=../test-report.html --self-contained-html || {
+          python -m pytest -v --html=./test-report.html --self-contained-html || { 

# After launching the app, take a screenshot
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png

# After all test steps, before script ends, zip all artifacts
cd ..
zip -r e2e-artifacts.zip test-report.html appium.log logcat.txt screenshots e2e_recording.mp4
cd Android-App-for-Car-Parking-Project 