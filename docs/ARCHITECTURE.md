# DataShield Architecture

## System Overview

DataShield is a three-tier web application designed with clean architecture principles:

```
┌─────────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                        │
│  Angular 17 Frontend (TypeScript, Bootstrap, Reactive)      │
└──────────────────────┬──────────────────────────────────────┘
                       │
                    HTTP/REST
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    API LAYER                                │
│  Spring Boot REST Controllers + OpenAPI/Swagger             │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                  BUSINESS LOGIC                             │
│  Services (Auth, Dataset, Policy, Job, Audit)              │
│  Anonymization Strategies (Pluggable)                       │
│  Security (JWT, RBAC)                                      │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                DATA ACCESS LAYER                            │
│  Spring Data JPA Repositories                               │
│  Hibernate ORM                                              │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    DATABASE                                 │
│  PostgreSQL 15 (Relational Data)                            │
└──────────────────────────────────────────────────────────────┘
```

## Backend Components

### 1. Controllers (`controller/`)
- **AuthController**: Login, token generation
- **DatasetController**: Upload, list, delete datasets
- **PolicyController**: Create and manage anonymization rules
- **JobController**: Trigger and monitor async jobs
- **AuditController**: Query audit logs

### 2. Services (`service/`)
- **AuthService**: JWT token generation and authentication
- **DatasetService**: Dataset lifecycle management
- **AuditService**: Event logging and tracking
- **AnonymizationStrategy**: Pluggable strategies (Mask, Hash, Suppress)

### 3. Domain Models (`domain/`)
- **User**: Users with roles (ADMIN, ANALYST, AUDITOR)
- **Dataset**: Uploaded data files with metadata
- **AnonymizationPolicy**: Rules defining anonymization per column
- **AnonymizationJob**: Execution records with status tracking
- **AuditLog**: Immutable event records

### 4. Security (`security/`)
- **JwtTokenProvider**: JWT generation and validation
- **JwtAuthenticationFilter**: Request authentication
- **CustomUserDetailsService**: User loading for Spring Security
- **SecurityConfig**: Global security configuration with CORS, session management

### 5. Worker (`worker/`)
- **AnonymizationJobWorker**: Scheduled task for processing queued jobs
- Job status transitions: QUEUED → RUNNING → SUCCEEDED/FAILED

### 6. Database (`db/migration/`)
- **V1__Initial_Schema.sql**: Tables and indexes
- **V2__Insert_Seed_Data.sql**: Demo users

## Frontend Components

### 1. Pages (`app/pages/`)
- **LoginComponent**: Authentication form
- **DashboardComponent**: Overview and statistics

### 2. Services (`app/services/`)
- **AuthService**: Login, token management
- **DatasetService**: CRUD operations on datasets
- **AuditService**: Fetch audit logs

### 3. Guards (`app/guards/`)
- **AuthGuard**: Route protection and role-based access

### 4. Models (`app/models/`)
- TypeScript interfaces for type safety

## Data Flow Example: Upload & Anonymize

```
1. User Upload Flow
   ┌──────────┐
   │  Browser │
   └────┬─────┘
        │ POST /api/datasets/upload (multipart)
        ▼
   ┌──────────────────┐
   │  DatasetController│
   └────┬─────────────┘
        │
        ▼
   ┌──────────────────┐
   │  DatasetService  │
   ├──────────────────┤
   │ - Save file      │
   │ - Extract headers│
   │ - Log event      │
   └────┬─────────────┘
        │
        ▼
   ┌──────────────────┐
   │   PostgreSQL     │
   │  (datasets table)│
   └──────────────────┘

2. Anonymization Job Flow
   ┌──────────┐
   │  Browser │
   └────┬─────┘
        │ POST /api/jobs (datasetId, policyId)
        ▼
   ┌──────────────────┐
   │  JobController   │
   └────┬─────────────┘
        │
        ▼
   ┌──────────────────────┐
   │  Database (INSERT)   │
   │  status: QUEUED      │
   └────┬─────────────────┘
        │
        │ (Polling)
        │
   ┌────▼──────────────────────┐
   │ JobWorker @Scheduled       │
   │ - Query QUEUED jobs       │
   │ - Apply anonymization     │
   │ - Update status/output    │
   │ - Log completion          │
   └────────────────────────────┘
```

## Key Design Patterns

### 1. Strategy Pattern
```java
AnonymizationStrategy {
  - MaskAnonymizationStrategy
  - HashAnonymizationStrategy
  - SuppressAnonymizationStrategy
}
```

### 2. Repository Pattern
```java
DatasetRepository extends JpaRepository<Dataset, Long>
```

### 3. Service Layer
- Encapsulates business logic
- Transaction management
- Separation of concerns

### 4. JWT-Based Authentication
- Stateless authentication
- Token-based API calls
- Role-based authorization

## Database Schema

### Key Tables
- **users**: Credentials and roles
- **datasets**: Uploaded files metadata
- **policies**: Anonymization rules (JSONB)
- **anonymization_jobs**: Job execution records
- **audit_logs**: Immutable event log

### Indexes
- `idx_datasets_owner`: Fast user dataset lookups
- `idx_policies_dataset`: Policy retrieval by dataset
- `idx_jobs_status`: Find jobs by status
- `idx_audit_user`: User activity tracking

## Deployment Architecture

```
Docker Host
├── postgres:15 (port 5432)
├── datashield-backend (port 8080)
│   └── Spring Boot App
└── datashield-frontend (port 4200)
    └── Angular App (Nginx)
```

## Security Measures

1. **Authentication**: JWT tokens with configurable expiry
2. **Authorization**: Spring Security with @PreAuthorize
3. **Password Hashing**: BCrypt with configurable rounds
4. **CORS**: Configured for specific origins
5. **HTTPS Ready**: Configuration for production TLS
6. **Audit Trail**: All actions logged with user context

## Performance Considerations

- **Database Indexing**: Optimized queries
- **Async Processing**: Non-blocking job execution
- **Connection Pooling**: Efficient DB connections
- **Caching Ready**: Structure supports caching layers
- **Scalability**: Stateless backend, horizontal scaling ready

## Future Architecture Extensions

- **Message Queue**: Replace DB-based queue with RabbitMQ/Kafka
- **Cache Layer**: Redis for frequently accessed data
- **Monitoring**: Micrometer metrics, centralized logging
- **API Versioning**: Support multiple API versions
- **Database Sharding**: Partition data for large scale

---

For more details, refer to the main [README.md](../README.md).
