# Local Development Setup

## Prerequisites

- **Java 21+**: [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Node.js 18+**: [Download](https://nodejs.org/)
- **PostgreSQL 15+**: [Download](https://www.postgresql.org/download/)
- **Maven 3.8+**: [Download](https://maven.apache.org/download.cgi)
- **Angular CLI 17+**: `npm install -g @angular/cli`
- **Git**: [Download](https://git-scm.com/downloads)

## Backend Setup

### 1. Database Setup

```bash
# Create PostgreSQL user and database
psql -U postgres

CREATE USER datashield WITH PASSWORD 'datashield123';
CREATE DATABASE datashield OWNER datashield;
GRANT ALL PRIVILEGES ON DATABASE datashield TO datashield;
```

### 2. Configure application.yml

Create `backend/src/main/resources/application-local.yml`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
  datasource:
    url: jdbc:postgresql://localhost:5432/datashield
    username: datashield
    password: datashield123

jwt:
  secret: your-local-dev-secret-key-32-chars-min
  expiration: 86400000

app:
  datastorage:
    path: ./data
```

### 3. Run Backend

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

Backend will be available at: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Frontend Setup

### 1. Install Dependencies

```bash
cd frontend
npm install
```

### 2. Development Server

```bash
ng serve --open
```

Frontend will open automatically at: `http://localhost:4200`

### 3. Build for Production

```bash
ng build --configuration production
```

Output: `frontend/dist/datashield-frontend/`

## Running Tests

### Backend Tests

```bash
cd backend
mvn test

# Specific test class
mvn test -Dtest=HashAnonymizationStrategyTest
```

### Frontend Tests

```bash
cd frontend
npm test
```

## Troubleshooting

### Database Connection Failed

```bash
# Test PostgreSQL connection
psql -U datashield -d datashield -h localhost

# View PostgreSQL logs
tail -f /usr/local/var/log/postgres.log
```

### Port Already in Use

```bash
# Change backend port in application.yml
server:
  port: 8081

# Change frontend port
ng serve --port 4201
```

### Maven Build Issues

```bash
# Clean cache
mvn clean

# Rebuild
mvn install
```

## Environment Variables

For local development, you can set:

```bash
export JAVA_HOME=/path/to/java21
export JWT_SECRET=local-dev-secret
export DATASTORAGE_PATH=./data
```

## IDE Setup

### IntelliJ IDEA

1. Open project folder
2. `File` → `Open`
3. Select `DataShield` folder
4. Trust project
5. Run → Run 'DataShieldApplication'

### VS Code

Backend:
- Install "Extension Pack for Java"
- Install "Spring Boot Extension Pack"

Frontend:
- Install "Angular Language Service"
- Install "Angular Schematics"

## Git Workflow

```bash
# Create feature branch
git checkout -b feature/my-feature

# Make changes and commit
git add .
git commit -m "feat: add my feature"

# Push to fork
git push origin feature/my-feature

# Create Pull Request on GitHub
```

---

Happy coding! 🚀
