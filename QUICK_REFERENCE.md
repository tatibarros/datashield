## 🚀 DataShield - Quick Reference

### 📍 Portas Padrão
| Serviço | Porta | URL |
|---------|-------|-----|
| Frontend | 4200 | http://localhost:4200 |
| Backend | 8080 | http://localhost:8080 |
| PostgreSQL | 5432 | localhost:5432 |
| Swagger | 8080 | http://localhost:8080/swagger-ui.html |

### 🔐 Demo Users
```
admin   / admin123
analyst / analyst123
auditor / auditor123
```

### 📦 Docker Compose Commands
```bash
# Start
docker-compose up --build

# Stop
docker-compose down

# View logs
docker-compose logs -f

# Restart service
docker-compose restart backend
```

### 🧪 Testing
```bash
# Backend tests
cd backend && mvn test

# Frontend tests
cd frontend && npm test
```

### 🛠️ Common Endpoints
```bash
# Login
POST /api/auth/login
Body: {"username":"admin","password":"admin123"}

# List datasets
GET /api/datasets
Headers: Authorization: Bearer {token}

# Upload dataset
POST /api/datasets/upload
Headers: Authorization: Bearer {token}
Body: FormData with file and name

# Get audit logs
GET /api/audit
Headers: Authorization: Bearer {token}
```

### 📁 Important Files
- `docker-compose.yml` - Service orchestration
- `backend/pom.xml` - Backend dependencies
- `frontend/package.json` - Frontend dependencies
- `docs/ARCHITECTURE.md` - System design
- `docs/LOCAL_DEVELOPMENT.md` - Dev setup
- `docs/TROUBLESHOOTING.md` - Problem solving

### 🔄 Git Workflow
```bash
git clone https://github.com/yourusername/datashield.git
cd datashield
git checkout -b feature/my-feature
# make changes
git commit -m "feat: description"
git push origin feature/my-feature
# open Pull Request
```

### 📊 Database Queries
```sql
-- Connect to PostgreSQL
psql -U datashield -d datashield -h localhost

-- View users
SELECT * FROM users;

-- View datasets
SELECT * FROM datasets;

-- View audit logs
SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT 10;

-- Delete test data (careful!)
DELETE FROM anonymization_jobs;
DELETE FROM datasets;
```

### 🔍 Useful Docker Commands
```bash
# View running containers
docker ps

# View all containers
docker ps -a

# View logs
docker logs {container_id}

# Execute command in container
docker exec -it {container_id} {command}

# Remove image
docker rmi {image_id}

# Remove volume
docker volume rm {volume_name}
```

### 📈 Performance Tips
- Use database indexes (already configured)
- Cache frequently accessed data
- Paginate large result sets
- Use async processing for long tasks
- Monitor resource usage

### 🔒 Security Tips
- Change default passwords before production
- Use strong JWT secrets (32+ chars)
- Enable HTTPS/TLS in production
- Configure CORS for specific domains
- Regular database backups
- Keep dependencies updated

### 📚 Documentation
- `/README.md` - Main documentation
- `/docs/ARCHITECTURE.md` - System design
- `/docs/LOCAL_DEVELOPMENT.md` - Development setup
- `/docs/CONTRIBUTING.md` - Contribution guidelines
- `/docs/TROUBLESHOOTING.md` - Common issues
- `/docs/images/` - Screenshots and diagrams

### 🎯 Next Steps
1. Clone and setup locally
2. Explore the codebase
3. Run tests to verify setup
4. Try uploading sample CSV
5. Test different user roles
6. Read architecture documentation
7. Add new features
8. Deploy to cloud (optional)

### 📞 Getting Help
1. Check `/docs/TROUBLESHOOTING.md`
2. Review `/docs/LOCAL_DEVELOPMENT.md`
3. Check Swagger UI for API docs
4. Open GitHub issue
5. Contact maintainers

---

**Made with ❤️ for data privacy**
