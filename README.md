# DataShield – Data Anonymization Platform (Portfolio Edition)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Angular Version](https://img.shields.io/badge/Angular-17-red)](https://angular.io/)
[![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-3.2-green)](https://spring.io/projects/spring-boot)

## 📋 About the Project

**DataShield** is an open-source, portfolio-ready platform for anonymizing sensitive data in CSV and JSON datasets. It demonstrates modern web development practices with a clean architecture, comprehensive API documentation, and a user-friendly interface.

This is a **production-inspired MVP** that showcases:
- ✅ Secure JWT authentication with role-based access control (RBAC)
- ✅ RESTful API with OpenAPI/Swagger documentation
- ✅ Asynchronous job processing for data anonymization
- ✅ Multiple anonymization strategies (Mask, Hash, Suppress)
- ✅ Comprehensive audit logging
- ✅ Full-stack application with modern frameworks
- ✅ Docker containerization for easy deployment
- ✅ Professional README and documentation

---

## 🎯 Key Features

### 1️⃣ **Data Upload & Profiling**
- Upload CSV/JSON datasets
- Automatic column type inference
- Row and column metadata tracking
- PII (Personally Identifiable Information) detection heuristics

### 2️⃣ **Anonymization Policies**
- Create named policies per dataset
- Multiple anonymization strategies:
  - **MASK**: Hide characters (e.g., `****12345`)
  - **HASH**: SHA-256 with salt
  - **SUPPRESS**: Remove or null values
  - **GENERALIZE**: Aggregate into ranges (age bands, etc.)
- Policy versioning

### 3️⃣ **Async Job Processing**
- Queue-based job system (via database + scheduler)
- Real-time status tracking (QUEUED → RUNNING → SUCCEEDED/FAILED)
- Detailed error messages and logs

### 4️⃣ **Results & Export**
- Preview anonymized data (limited to 50 rows)
- Download anonymized datasets as CSV
- Before/after comparison

### 5️⃣ **Audit Trail**
- Log all actions: login, uploads, policy changes, job execution
- Filter by user, action, or date
- IP address and timestamp tracking

### 6️⃣ **Role-Based Access**
- **ADMIN**: Full system control
- **ANALYST**: Create and manage datasets/policies
- **AUDITOR**: View-only access to logs

---

## 🏗️ Architecture

### Backend Stack
```
├── Controller Layer (REST APIs)
├── Service Layer (Business Logic)
├── Worker (Async Processing)
├── Repository Layer (Data Access)
├── Security (JWT + Spring Security)
└── Audit (Event Logging)
```

### Frontend Stack
```
├── Pages (Views)
├── Components (Reusable UI)
├── Services (API Communication)
├── Guards (Route Protection)
└── Models (TypeScript Interfaces)
```

### Data Flow Diagram
```
┌─────────────┐
│   Angular   │ (Frontend - Browser)
│  Frontend   │
└──────┬──────┘
       │
       ├─── HTTP/REST ────────┐
       │                      │
       │                  ┌───▼──────────────┐
       │                  │  Spring Boot     │
       │                  │  Backend API     │
       │                  └───┬──────────────┘
       │                      │
       │              ┌───────┼──────────┐
       │              │       │          │
       │          ┌───▼──┐ ┌──▼──┐  ┌──▼──────┐
       │          │  JWT │ │Auth │  │ Database│
       │          │Token │ │     │  │PostgreSQL
       │          └──────┘ └─────┘  └─────────┘
       │                      │
       │              ┌───────▼─────────┐
       │              │  Job Scheduler  │
       │              │ (Anonymization) │
       │              └─────────────────┘
```

---

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose installed
- Git installed

### Setup & Run (3 Steps)

#### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/datashield.git
cd datashield
```

#### 2. Start Services with Docker Compose
```bash
docker-compose up --build
```

This will:
- Start PostgreSQL database
- Build and run Spring Boot backend (port 8080)
- Build and run Angular frontend (port 4200)

#### 3. Access the Application
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger/OpenAPI Docs**: http://localhost:8080/swagger-ui.html

---

## 🔐 Demo Credentials

Use these credentials to log in:

| Role    | Username | Password     |
|---------|----------|--------------|
| Admin   | admin    | admin123     |
| Analyst | analyst  | analyst123   |
| Auditor | auditor  | auditor123   |

---

## 📊 Sample Data

Sample CSV files are included in `/sample-data`:

- **customers.csv**: 10 customer records with PII (email, phone, CPF)
- **transactions.csv**: 10 transaction records with sensitive data (card numbers)

Upload these to test the platform!

---

## 📚 API Documentation

All endpoints are documented via **Swagger/OpenAPI** at:
```
http://localhost:8080/swagger-ui.html
```

### Key Endpoints

#### Authentication
- `POST /api/auth/login` - Login and get JWT token
- `GET /api/auth/health` - Health check

#### Datasets
- `POST /api/datasets/upload` - Upload new dataset
- `GET /api/datasets` - List user's datasets
- `GET /api/datasets/{id}` - Get dataset details
- `DELETE /api/datasets/{id}` - Delete dataset

#### Policies
- `GET /api/policies/dataset/{datasetId}` - List policies for dataset
- `GET /api/policies/{id}` - Get policy details

#### Jobs
- `POST /api/jobs` - Start an anonymization job (params: datasetId, policyId)
- `GET /api/jobs/dataset/{datasetId}` - List jobs for dataset
- `GET /api/jobs/{id}` - Get job status
- `GET /api/jobs/status/queued` - List queued jobs

#### Audit
- `GET /api/audit` - Get recent audit logs
- `GET /api/audit/user/{userId}` - Get user's audit logs
- `GET /api/audit/action/{action}` - Filter by action

---

## 🛠️ Tech Stack Summary

| Component | Technology | Version |
|-----------|-----------|---------|
| **Backend Framework** | Spring Boot | 3.2.x |
| **Language** | Java | 21 |
| **Database** | PostgreSQL | 15 |
| **ORM** | Hibernate/JPA | - |
| **Security** | Spring Security + JWT | - |
| **Async** | Spring @Scheduled | - |
| **Frontend Framework** | Angular | 17+ |
| **UI Library** | Bootstrap | 5.3 |
| **API Docs** | OpenAPI/Swagger | 3.x |
| **Container** | Docker | - |
| **Orchestration** | Docker Compose | - |

---

## 📁 Project Structure

```
datashield/
├── backend/
│   ├── src/main/java/com/datashield/
│   │   ├── controller/        # REST endpoints
│   │   ├── service/           # Business logic
│   │   ├── domain/            # JPA entities
│   │   ├── repository/        # Data access
│   │   ├── security/          # JWT, auth
│   │   ├── worker/            # Async jobs
│   │   ├── audit/             # Event logging
│   │   └── dto/               # Data transfer objects
│   ├── src/main/resources/
│   │   ├── db/migration/      # Flyway SQL migrations
│   │   └── application.yml    # Config
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/         # Route components
│   │   │   ├── components/    # Reusable UI
│   │   │   ├── services/      # API calls
│   │   │   ├── guards/        # Route guards
│   │   │   ├── models/        # Interfaces
│   │   │   └── app.routes.ts  # Routing config
│   │   ├── index.html
│   │   └── main.ts
│   ├── package.json
│   ├── angular.json
│   ├── Dockerfile
│   └── nginx.conf
├── sample-data/
│   ├── customers.csv
│   └── transactions.csv
├── docs/
│   ├── ARCHITECTURE.md
│   ├── CONTRIBUTING.md
│   └── images/
├── docker-compose.yml
└── README.md (this file)

```

---

## ✅ Testing

### Run Backend Unit Tests
```bash
cd backend
mvn test
```

Tests cover:
- `MaskAnonymizationStrategy` - Masking logic
- `HashAnonymizationStrategy` - Hashing consistency

### Run Frontend Tests (Optional)
```bash
cd frontend
npm test
```

---

## 🔧 Configuration

### Environment Variables

Backend (in `docker-compose.yml`):
```yaml
DB_HOST=postgres
DB_PORT=5432
DB_NAME=datashield
DB_USER=datashield
DB_PASSWORD=datashield123
JWT_SECRET=your-secret-key-change-in-production
CORS_ORIGINS=http://localhost:4200
```

Frontend (in `nginx.conf`):
- API proxy to backend on port 8080

### Local Development (Without Docker)

**Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
ng serve
```

Then access: `http://localhost:4200`

---

## 📖 Roadmap (Future Enhancements)

- [ ] Excel (.xlsx) file support
- [ ] Advanced PII detection (regex patterns, ML-based)
- [ ] More anonymization strategies (Perturbation, K-anonymity)
- [ ] Data profiling visualizations (histograms, distributions)
- [ ] Batch job processing and scheduling
- [ ] Data retention and automatic cleanup policies
- [ ] Integration tests for full workflow
- [ ] Performance optimizations for large datasets
- [ ] User management UI for ADMIN role
- [ ] Multi-language support

---

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](docs/CONTRIBUTING.md) for guidelines.

### How to Contribute
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m 'Add feature'`
4. Push: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📝 License

This project is licensed under the **MIT License** – see [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Tatiana Alves**
- GitHub: [@tatibarros](https://github.com/tatibarros)
- LinkedIn: [Tatiana Alves](https://linkedin.com/in/tatianabalves)

---

## 🙏 Acknowledgments

- Spring Boot & Spring Security documentation
- Angular documentation and best practices
- Bootstrap for UI components
- PostgreSQL for reliability

---

## 📞 Support

For issues, questions, or feedback:
- Open an issue on GitHub
- Check existing documentation in `/docs`
- Review API docs at `/swagger-ui.html`

---

## ⭐ If you found this helpful, please star the repository!

```
Made with ❤️ for the data privacy community
```

