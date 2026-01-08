#!/bin/bash

# DataShield - Quick Start Script

echo "🛡️  DataShield - Data Anonymization Platform"
echo "=============================================="
echo ""

# Check Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "✅ Docker and Docker Compose found"
echo ""
echo "Starting DataShield services..."
echo ""

# Start services
docker-compose up --build

echo ""
echo "=============================================="
echo "✅ DataShield is running!"
echo ""
echo "📱 Frontend: http://localhost:4200"
echo "🔌 Backend API: http://localhost:8080"
echo "📚 API Docs: http://localhost:8080/swagger-ui.html"
echo ""
echo "Demo Credentials:"
echo "  Username: admin | Password: admin123"
echo "  Username: analyst | Password: analyst123"
echo "  Username: auditor | Password: auditor123"
echo ""
echo "=============================================="
