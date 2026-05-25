# DataShield – Architecture Overview

## 1. Component Diagram

```
[Angular Frontend]
   |
HTTP REST
   |
[Spring Boot API Backend] – [Job Worker]
   |
[PostgreSQL Database]
```

- **Frontend** (Angular 17+): SPA, authentication via JWT, role-aware navigation, REST calls
- **Backend** (Spring Boot 3.2): REST APIs, RBAC, async jobs, audit logs
- **Job Worker**: Spring @Scheduled for queue processing
- **Database**: PostgreSQL 15, modeled for datasets, policies, jobs, audit

## 2. Backend Layers
- **Controller**: Exposes REST endpoints, validates input
- **Service**: Orchestrates business logic (policy engine, audit, etc)
- **Worker**: Runs async jobs from DB queue
- **Repository**: Data access via JPA/Hibernate
- **Security**: JWT parsing, RBAC via Spring Security
- **Audit**: Every user action and job logged with userId, action, resource, IP, and timestamp

## 3. Frontend Layers
- **Pages & Components**: Views and reusable UI
- **Services**: Talks to backend API
- **Guards**: Protects routes by user role
- **Models**: TypeScript interfaces representing API payloads

## 4. Data Workflow
- User logs in
- Uploads file (or picks demo)
- System parses and profiles data, detects PII columns
- Analyst creates anonymization policy (per column)
- Submits job → Async worker runs, writes result to DB
- Analyst/Auditor previews/downloads anonymized data
- Every action is audited

## 5. JWT Auth Flow
- POST `/api/auth/login` with {username, password} → get JWT
- Attach JWT as `Authorization: Bearer ...` in all API requests
- RBAC enforced (ADMIN full; ANALYST manage data; AUDITOR read-only logs)

## 6. Deployment
- **Docker Compose** orchestrates DB, backend, frontend
- Exposed:
   - Angular: http://localhost:4200
   - Spring Boot API: http://localhost:8080
   - Swagger: http://localhost:8080/swagger-ui.html

## 7. Data Model (Simplified)
- **User**(id, username, password_hash, role)
- **Dataset**(id, owner_id, name, metadata)
- **Column**(id, dataset_id, name, type, is_pii, pii_type)
- **Policy**(id, dataset_id, name, version, rules[])
- **Job**(id, dataset_id, policy_id, user_id, status, timestamps)
- **AuditLog**(id, user_id, action, resource_type, resource_id, ip, ts)

## 8. Security & Best Practices
- Passwords hashed (BCrypt)
- JWT tokens, short-lived, rotated on login
- CORS: lock to frontend origin/localdev
- Audit log immutable (soft delete only)

---
_Last updated: 2026-05-25_
