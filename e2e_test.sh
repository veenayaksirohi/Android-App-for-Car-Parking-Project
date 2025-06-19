-          python -m pytest -v --html=../test-report.html --self-contained-html || {
+          python -m pytest -v --html=./test-report.html --self-contained-html || { 

# After launching the app, take a screenshot
adb shell screencap -p /sdcard/app_launch.png
adb pull /sdcard/app_launch.png screenshots/app_launch.png

# ... rest of your test steps ...

# At the end, zip all artifacts
cd ..
zip -r e2e-artifacts.zip Android-App-for-Car-Parking-Project/test-report.html Android-App-for-Car-Parking-Project/appium.log Android-App-for-Car-Parking-Project/logcat.txt Android-App-for-Car-Parking-Project/screenshots Android-App-for-Car-Parking-Project/e2e_recording.mp4
cd Android-App-for-Car-Parking-Project