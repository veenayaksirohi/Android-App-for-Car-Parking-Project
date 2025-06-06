$content = Get-Content 'gradlew' -Raw
$content = $content -replace "`r`n", "`n"
$content = $content -replace "`r", "`n"
[System.IO.File]::WriteAllText('gradlew', $content, [System.Text.Encoding]::UTF8)
Write-Host "Fixed line endings in gradlew"

