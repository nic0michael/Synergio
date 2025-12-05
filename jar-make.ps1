# Build the Synergeio Spring Boot application using Gradle

& .\gradlew.bat build

if ($LASTEXITCODE -ne 0) {
    Write-Error "Build failed with exit code $LASTEXITCODE"
    exit $LASTEXITCODE
}

Write-Host "Build completed successfully" -ForegroundColor Green
