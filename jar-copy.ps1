# Copy and rename the built JAR file for deployment

$sourcePath = Get-ChildItem "build\libs\*-0.0.1-SNAPSHOT.jar" -Exclude "*-plain.jar" | Select-Object -First 1 -ExpandProperty FullName
$destinationPath = "synergio.jar"

if (-not $sourcePath -or -not (Test-Path $sourcePath)) {
    Write-Error "Source JAR file not found in build\libs\"
    Write-Host "Please run jar-make.ps1 first to build the application" -ForegroundColor Yellow
    exit 1
}

Copy-Item -Path $sourcePath -Destination $destinationPath -Force

Write-Host "JAR file copied successfully to $destinationPath" -ForegroundColor Green
