@echo off
REM DataShield - Quick Start Script for Windows

echo.
echo 🛡️  DataShield - Data Anonymization Platform
echo =============================================
echo.

REM Check Docker
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker is not installed. Please install Docker Desktop.
    exit /b 1
)

docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker Compose is not installed. Please install Docker Desktop.
    exit /b 1
)

echo ✅ Docker and Docker Compose found
echo.
echo Starting DataShield services...
echo.

docker-compose up --build

echo.
echo =============================================
echo ✅ DataShield is running!
echo.
echo 📱 Frontend: http://localhost:4200
echo 🔌 Backend API: http://localhost:8080
echo 📚 API Docs: http://localhost:8080/swagger-ui.html
echo.
echo Demo Credentials:
echo   Username: admin ^| Password: admin123
echo   Username: analyst ^| Password: analyst123
echo   Username: auditor ^| Password: auditor123
echo.
echo =============================================
pause
