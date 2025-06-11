# Create a temporary directory
$tempDir = "C:\temp\gradle"
New-Item -ItemType Directory -Force -Path $tempDir

# Download Gradle
$gradleVersion = "8.9"
$gradleUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
$gradleZip = "$tempDir\gradle.zip"
Invoke-WebRequest -Uri $gradleUrl -OutFile $gradleZip

# Extract Gradle
Expand-Archive -Path $gradleZip -DestinationPath $tempDir -Force

# Add Gradle to PATH
$gradleBin = "$tempDir\gradle-$gradleVersion\bin"
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
if (-not $currentPath.Contains($gradleBin)) {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$gradleBin", "User")
}

Write-Host "Gradle has been installed successfully!" 