# DataShield – Project Brief

## Vision & Purpose
DataShield is a modern, portfolio-ready platform for anonymizing sensitive data in CSV and JSON datasets. It demonstrates secure, scalable, and maintainable architectures following industry best practices for web development.

## Problem Statement
Organizations need to anonymize PII in large datasets for compliance (LGPD, GDPR) and analytics while preserving data utility. This project is an MVP and educational showcase.

## Core Functionality
- **Secure authentication** (JWT, RBAC)
- **Dataset upload** (CSV, JSON)
- **PII detection and profiling**
- **Configurable anonymization policies** (mask, hash, suppress, generalize)
- **Asynchronous job queues for processing**
- **Full audit trail with granular logging**
- **Result preview and export**
- **Role-based access:**
  - ADMIN: full control
  - ANALYST: manage datasets and policies
  - AUDITOR: logs, view only

## Tech Stack
| Layer         | Technology            |
|--------------|----------------------|
| Frontend     | Angular 17+, Bootstrap|
| Backend      | Spring Boot 3.2, Java 21|
| Auth         | JWT, Spring Security |
| Database     | PostgreSQL 15        |
| Container    | Docker, Docker Compose|

## High-Level Architecture
- **Backend**: RESTful API (Controller → Service → Worker → Repository)
- **Frontend**: Angular SPA, RxJS, Guards, Material/Bootstrap UI
- **Database**: PostgreSQL, strong typing, migration (Flyway)
- **Async/Jobs**: Spring @Scheduled, DB job table

## Sample User Journey
1. Login as Analyst (user: analyst / pass: analyst123)
2. Upload CSV (sample-data/customers.csv)
3. Detect columns/PII
4. Create anonymization policy per column
5. Submit job, system processes in background
6. Download/preview anonymized result
7. Review audit logs as Auditor

## Provided Credentials (Demo)
| Role    | Username | Password   |
|---------|----------|------------|
| Admin   | admin    | admin123   |
| Analyst | analyst  | analyst123 |
| Auditor | auditor  | auditor123 |

## API Reference
- `/api/auth/login` – Authenticate
- `/api/datasets/upload` – Upload file
- `/api/policies` – Manage policies
- `/api/jobs` – Run anonymization
- `/api/audit` – Logs endpoint

## Sample Data Path
- `sample-data/customers.csv`: 10 fictional customer records
- `sample-data/transactions.csv`: 10 fictional transactions

## Quickstart
1. `git clone https://github.com/tatibarros/datashield`
2. `docker-compose up --build`
3. Frontend: http://localhost:4200
4. Backend: http://localhost:8080

---
Last updated: 2026-05-25