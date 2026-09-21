while ($true) {
    $proc = Get-Process java -ErrorAction SilentlyContinue
    if (-not $proc) { break }
    Start-Sleep -Seconds 2
}
Copy-Item app\build\outputs\apk\debug\app-debug.apk releases\UnsulliedCode-debug.apk -Force
git add -A
git commit -m "Update app launcher logo to custom uploaded design"
git push origin main
