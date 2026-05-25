# DataShield - API Endpoints Reference

## 📋 Overview

All endpoints are secured with JWT authentication (except `/api/auth/login`). Include the JWT token in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

**Base URL**: `http://localhost:8080/api`

**API Documentation (Swagger/OpenAPI)**: `http://localhost:8080/swagger-ui.html`

---

## 🔐 Authentication Endpoints

### POST `/auth/login`
**Description**: Authenticate user and receive JWT token

**Authentication**: None (public endpoint)

**Request**:
```json
{
  "username": "analyst",
  "password": "analyst123"
}
```

**Response (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwibmFtZSI6ImFuYWx5c3QiLCJyb2xlIjoiQU5BTFlTVCIsImlhdCI6MTY4NDk5Mjc0MCwiZXhwIjoxNjg1MDc5MTQwfQ.abcd1234...",
  "userId": 2,
  "username": "analyst",
  "role": "ANALYST",
  "expiresIn": 86400
}
```

**Error (401 Unauthorized)**:
```json
{
  "status": 401,
  "message": "Invalid username or password",
  "timestamp": "2026-05-25T14:30:00Z"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"analyst","password":"analyst123"}'
```

---

### GET `/auth/health`
**Description**: Check backend health status

**Authentication**: None (public endpoint)

**Response (200 OK)**:
```json
{
  "status": "UP",
  "timestamp": "2026-05-25T14:35:00Z",
  "version": "1.0.0"
}
```

---

## 📂 Dataset Endpoints

### POST `/datasets/upload`
**Description**: Upload a new dataset (CSV or JSON)

**Authentication**: Required (ANALYST, ADMIN)

**Request**: Multipart form data
```
POST /datasets/upload
Content-Type: multipart/form-data

file: <binary CSV/JSON file>
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "name": "customers.csv",
  "fileSize": 2048,
  "fileType": "csv",
  "rowCount": 10,
  "columnCount": 5,
  "columns": [
    {
      "id": 1,
      "name": "id",
      "type": "INTEGER",
      "isPii": false,
      "piiType": null
    },
    {
      "id": 2,
      "name": "email",
      "type": "STRING",
      "isPii": true,
      "piiType": "EMAIL"
    },
    {
      "id": 3,
      "name": "phone",
      "type": "STRING",
      "isPii": true,
      "piiType": "PHONE"
    },
    {
      "id": 4,
      "name": "cpf",
      "type": "STRING",
      "isPii": true,
      "piiType": "CPF"
    },
    {
      "id": 5,
      "name": "age",
      "type": "INTEGER",
      "isPii": false,
      "piiType": null
    }
  ],
  "userId": 2,
  "createdAt": "2026-05-25T14:40:00Z",
  "updatedAt": "2026-05-25T14:40:00Z"
}
```

**Error (400 Bad Request)**:
```json
{
  "status": 400,
  "message": "Invalid file format. Supported: CSV, JSON",
  "timestamp": "2026-05-25T14:40:00Z"
}
```

**Error (413 Payload Too Large)**:
```json
{
  "status": 413,
  "message": "File size exceeds 100MB limit",
  "timestamp": "2026-05-25T14:40:00Z"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/datasets/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@customers.csv"
```

---

### GET `/datasets`
**Description**: List all datasets for the current user

**Authentication**: Required (all roles)

**Query Parameters**:
- `page` (optional, default: 0): Page number (0-indexed)
- `size` (optional, default: 20): Page size
- `sortBy` (optional, default: "createdAt"): Sort field (createdAt, name, rowCount)
- `sortOrder` (optional, default: "DESC"): Sort order (ASC, DESC)

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "name": "customers.csv",
      "fileSize": 2048,
      "fileType": "csv",
      "rowCount": 10,
      "columnCount": 5,
      "userId": 2,
      "createdAt": "2026-05-25T14:40:00Z",
      "updatedAt": "2026-05-25T14:40:00Z"
    },
    {
      "id": 2,
      "name": "transactions.json",
      "fileSize": 3072,
      "fileType": "json",
      "rowCount": 15,
      "columnCount": 8,
      "userId": 2,
      "createdAt": "2026-05-25T15:00:00Z",
      "updatedAt": "2026-05-25T15:00:00Z"
    }
  ],
  "totalElements": 2,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 20
}
```

**cURL Example**:
```bash
curl -X GET "http://localhost:8080/api/datasets?page=0&size=20" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/datasets/{datasetId}`
**Description**: Get detailed information about a specific dataset

**Authentication**: Required (all roles)

**Path Parameters**:
- `datasetId`: Dataset ID (integer)

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "customers.csv",
  "fileSize": 2048,
  "fileType": "csv",
  "rowCount": 10,
  "columnCount": 5,
  "columns": [
    {
      "id": 1,
      "name": "id",
      "type": "INTEGER",
      "isPii": false,
      "piiType": null
    },
    {
      "id": 2,
      "name": "email",
      "type": "STRING",
      "isPii": true,
      "piiType": "EMAIL"
    }
  ],
  "userId": 2,
  "createdAt": "2026-05-25T14:40:00Z",
  "updatedAt": "2026-05-25T14:40:00Z"
}
```

**Error (404 Not Found)**:
```json
{
  "status": 404,
  "message": "Dataset not found",
  "timestamp": "2026-05-25T14:40:00Z"
}
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/datasets/1 \
  -H "Authorization: Bearer <token>"
```

---

### GET `/datasets/{datasetId}/preview`
**Description**: Get preview of dataset data (first 50 rows)

**Authentication**: Required (all roles)

**Query Parameters**:
- `limit` (optional, default: 50): Number of rows to preview

**Response (200 OK)**:
```json
{
  "data": [
    {
      "id": "1",
      "email": "john@example.com",
      "phone": "11987654321",
      "cpf": "12345678901",
      "age": "28"
    },
    {
      "id": "2",
      "email": "jane@example.com",
      "phone": "11987654322",
      "cpf": "12345678902",
      "age": "35"
    }
  ],
  "rowCount": 2,
  "columnCount": 5
}
```

---

### DELETE `/datasets/{datasetId}`
**Description**: Delete a dataset (soft delete, preserves audit trail)

**Authentication**: Required (ANALYST, ADMIN - owner only)

**Response (204 No Content)**:
(No response body)

**Error (403 Forbidden)**:
```json
{
  "status": 403,
  "message": "You don't have permission to delete this dataset",
  "timestamp": "2026-05-25T14:40:00Z"
}
```

**cURL Example**:
```bash
curl -X DELETE http://localhost:8080/api/datasets/1 \
  -H "Authorization: Bearer <token>"
```

---

## 🎯 Policy Endpoints

### POST `/policies`
**Description**: Create a new anonymization policy

**Authentication**: Required (ANALYST, ADMIN)

**Request**:
```json
{
  "datasetId": 1,
  "name": "Customer_PII_Policy_v1",
  "description": "Anonymize customer PII",
  "rules": [
    {
      "columnName": "email",
      "strategy": "HASH",
      "parameters": {
        "salt": "app-salt-123"
      }
    },
    {
      "columnName": "phone",
      "strategy": "MASK",
      "parameters": {
        "showLastN": 4
      }
    },
    {
      "columnName": "cpf",
      "strategy": "SUPPRESS"
    },
    {
      "columnName": "age",
      "strategy": "GENERALIZE",
      "parameters": {
        "ranges": [[0,17], [18,25], [26,35], [36,50], [51,999]]
      }
    }
  ]
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "datasetId": 1,
  "name": "Customer_PII_Policy_v1",
  "description": "Anonymize customer PII",
  "version": 1,
  "rules": [
    {
      "id": 1,
      "columnName": "email",
      "strategy": "HASH",
      "parameters": {
        "salt": "app-salt-123"
      }
    },
    {
      "id": 2,
      "columnName": "phone",
      "strategy": "MASK",
      "parameters": {
        "showLastN": 4
      }
    },
    {
      "id": 3,
      "columnName": "cpf",
      "strategy": "SUPPRESS"
    },
    {
      "id": 4,
      "columnName": "age",
      "strategy": "GENERALIZE",
      "parameters": {
        "ranges": [[0,17], [18,25], [26,35], [36,50], [51,999]]
      }
    }
  ],
  "userId": 2,
  "createdAt": "2026-05-25T14:45:00Z",
  "updatedAt": "2026-05-25T14:45:00Z"
}
```

**Supported Strategies**:
- `MASK`: Hides characters. Example: `john@example.com` → `****@example.com`
- `HASH`: SHA-256 hash with salt (irreversible)
- `SUPPRESS`: Remove/null values
- `GENERALIZE`: Map to ranges. Example: age 28 → "26-35"

**Error (400 Bad Request)**:
```json
{
  "status": 400,
  "message": "Column 'unknown_column' does not exist in dataset",
  "timestamp": "2026-05-25T14:45:00Z"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/policies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{...}'
```

---

### GET `/policies/dataset/{datasetId}`
**Description**: List all policies for a dataset

**Authentication**: Required (all roles)

**Response (200 OK)**:
```json
{
  "data": [
    {
      "id": 1,
      "datasetId": 1,
      "name": "Customer_PII_Policy_v1",
      "version": 1,
      "ruleCount": 4,
      "userId": 2,
      "createdAt": "2026-05-25T14:45:00Z",
      "updatedAt": "2026-05-25T14:45:00Z"
    }
  ],
  "totalCount": 1
}
```

---

### GET `/policies/{policyId}`
**Description**: Get detailed information about a policy

**Authentication**: Required (all roles)

**Response (200 OK)**:
```json
{
  "id": 1,
  "datasetId": 1,
  "name": "Customer_PII_Policy_v1",
  "description": "Anonymize customer PII",
  "version": 1,
  "rules": [
    {
      "id": 1,
      "columnName": "email",
      "strategy": "HASH",
      "parameters": {
        "salt": "app-salt-123"
      }
    }
  ],
  "userId": 2,
  "createdAt": "2026-05-25T14:45:00Z",
  "updatedAt": "2026-05-25T14:45:00Z"
}
```

---

### PUT `/policies/{policyId}`
**Description**: Update an existing policy (creates new version)

**Authentication**: Required (ANALYST, ADMIN - owner only)

**Request**:
```json
{
  "name": "Customer_PII_Policy_v2",
  "description": "Updated policy with additional rules",
  "rules": [
    {
      "columnName": "email",
      "strategy": "HASH",
      "parameters": {
        "salt": "app-salt-456"
      }
    }
  ]
}
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "datasetId": 1,
  "name": "Customer_PII_Policy_v2",
  "version": 2,
  "rules": [...]
}
```

---

### DELETE `/policies/{policyId}`
**Description**: Delete a policy

**Authentication**: Required (ANALYST, ADMIN - owner only)

**Response (204 No Content)**:
(No response body)

---

## 🔄 Job Endpoints

### POST `/jobs`
**Description**: Submit a new anonymization job

**Authentication**: Required (ANALYST, ADMIN)

**Request**:
```json
{
  "datasetId": 1,
  "policyId": 1
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "datasetId": 1,
  "policyId": 1,
  "userId": 2,
  "status": "QUEUED",
  "inputRowCount": 10,
  "processingTimeMs": null,
  "createdAt": "2026-05-25T14:50:00Z",
  "updatedAt": "2026-05-25T14:50:00Z",
  "startedAt": null,
  "completedAt": null
}
```

**Status Values**:
- `QUEUED`: Waiting to be processed
- `RUNNING`: Currently being processed
- `SUCCEEDED`: Completed successfully
- `FAILED`: Failed with error

---

### GET `/jobs/dataset/{datasetId}`
**Description**: List all jobs for a dataset

**Authentication**: Required (all roles)

**Query Parameters**:
- `status` (optional): Filter by status (QUEUED, RUNNING, SUCCEEDED, FAILED)
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20): Page size

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "datasetId": 1,
      "policyId": 1,
      "userId": 2,
      "status": "SUCCEEDED",
      "inputRowCount": 10,
      "processingTimeMs": 245,
      "createdAt": "2026-05-25T14:50:00Z",
      "updatedAt": "2026-05-25T14:50:02Z",
      "startedAt": "2026-05-25T14:50:01Z",
      "completedAt": "2026-05-25T14:50:02Z",
      "errorMessage": null
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 20
}
```

---

### GET `/jobs/{jobId}`
**Description**: Get status and details of a specific job

**Authentication**: Required (all roles)

**Response (200 OK)**:
```json
{
  "id": 1,
  "datasetId": 1,
  "policyId": 1,
  "userId": 2,
  "status": "SUCCEEDED",
  "inputRowCount": 10,
  "processingTimeMs": 245,
  "createdAt": "2026-05-25T14:50:00Z",
  "updatedAt": "2026-05-25T14:50:02Z",
  "startedAt": "2026-05-25T14:50:01Z",
  "completedAt": "2026-05-25T14:50:02Z",
  "errorMessage": null
}
```

---

### GET `/jobs/{jobId}/preview`
**Description**: Get preview of anonymized results (first 50 rows)

**Authentication**: Required (all roles)

**Query Parameters**:
- `limit` (optional, default: 50): Number of rows to preview

**Response (200 OK)**:
```json
{
  "data": [
    {
      "id": "1",
      "email": "a1b2c3d4e5f6g7h8...",
      "phone": "****4321",
      "cpf": null,
      "age": "26-35"
    },
    {
      "id": "2",
      "email": "b2c3d4e5f6g7h8i9...",
      "phone": "****5432",
      "cpf": null,
      "age": "35-50"
    }
  ],
  "rowCount": 2,
  "columnCount": 5
}
```

---

### GET `/jobs/{jobId}/download`
**Description**: Download anonymized results as CSV

**Authentication**: Required (all roles)

**Query Parameters**:
- `format` (optional, default: "csv"): Output format (csv, json)

**Response (200 OK)**:
```
Content-Type: text/csv
Content-Disposition: attachment; filename="anonymized_data_2026-05-25_14-50-02.csv"

[CSV file content]
```

**cURL Example**:
```bash
curl -X GET "http://localhost:8080/api/jobs/1/download" \
  -H "Authorization: Bearer <token>" \
  -o anonymized_data.csv
```

---

### GET `/jobs/status/queued`
**Description**: List all currently queued jobs (ADMIN only)

**Authentication**: Required (ADMIN)

**Response (200 OK)**:
```json
{
  "data": [
    {
      "id": 5,
      "datasetId": 3,
      "policyId": 2,
      "userId": 2,
      "status": "QUEUED",
      "createdAt": "2026-05-25T15:00:00Z"
    }
  ],
  "totalCount": 1
}
```

---

### PATCH `/jobs/{jobId}/cancel`
**Description**: Cancel a queued job

**Authentication**: Required (ANALYST, ADMIN - owner only)

**Response (200 OK)**:
```json
{
  "id": 5,
  "status": "CANCELLED",
  "message": "Job cancelled successfully"
}
```

**Error (400 Bad Request)**:
```json
{
  "status": 400,
  "message": "Cannot cancel job with status SUCCEEDED",
  "timestamp": "2026-05-25T15:00:00Z"
}
```

---

## 📊 Audit Endpoints

### GET `/audit`
**Description**: Get recent audit logs

**Authentication**: Required (all roles - AUDITOR/ADMIN see all, others see own)

**Query Parameters**:
- `page` (optional, default: 0): Page number
- `size` (optional, default: 50): Page size
- `action` (optional): Filter by action type
- `userId` (optional): Filter by user ID
- `startDate` (optional): Start date (ISO format)
- `endDate` (optional): End date (ISO format)

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "userId": 2,
      "username": "analyst",
      "action": "LOGIN",
      "resourceType": "USER",
      "resourceId": "2",
      "ipAddress": "192.168.1.100",
      "timestamp": "2026-05-25T14:30:00Z",
      "details": {
        "userAgent": "Mozilla/5.0...",
        "sessionId": "sess-123"
      }
    },
    {
      "id": 2,
      "userId": 2,
      "username": "analyst",
      "action": "DATASET_UPLOADED",
      "resourceType": "DATASET",
      "resourceId": "1",
      "ipAddress": "192.168.1.100",
      "timestamp": "2026-05-25T14:40:00Z",
      "details": {
        "fileName": "customers.csv",
        "fileSize": 2048,
        "rowCount": 10
      }
    },
    {
      "id": 3,
      "userId": 2,
      "username": "analyst",
      "action": "POLICY_CREATED",
      "resourceType": "POLICY",
      "resourceId": "1",
      "ipAddress": "192.168.1.100",
      "timestamp": "2026-05-25T14:45:00Z",
      "details": {
        "policyName": "Customer_PII_Policy_v1",
        "ruleCount": 4
      }
    },
    {
      "id": 4,
      "userId": 2,
      "username": "analyst",
      "action": "JOB_SUBMITTED",
      "resourceType": "JOB",
      "resourceId": "1",
      "ipAddress": "192.168.1.100",
      "timestamp": "2026-05-25T14:50:00Z",
      "details": {
        "datasetId": "1",
        "policyId": "1",
        "expectedRowCount": 10
      }
    },
    {
      "id": 5,
      "userId": 2,
      "username": "analyst",
      "action": "JOB_SUCCEEDED",
      "resourceType": "JOB",
      "resourceId": "1",
      "ipAddress": "system",
      "timestamp": "2026-05-25T14:50:02Z",
      "details": {
        "processingTimeMs": 245,
        "rowsProcessed": 10,
        "successRate": "100%"
      }
    },
    {
      "id": 6,
      "userId": 2,
      "username": "analyst",
      "action": "RESULTS_DOWNLOADED",
      "resourceType": "JOB",
      "resourceId": "1",
      "ipAddress": "192.168.1.100",
      "timestamp": "2026-05-25T14:55:00Z",
      "details": {
        "format": "csv",
        "fileName": "anonymized_data_2026-05-25.csv"
      }
    }
  ],
  "totalElements": 6,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 50
}
```

**Audit Actions**:
- `LOGIN`: User logged in
- `LOGOUT`: User logged out
- `DATASET_UPLOADED`: New dataset uploaded
- `POLICY_CREATED`: New policy created
- `POLICY_UPDATED`: Policy modified
- `POLICY_DELETED`: Policy deleted
- `JOB_SUBMITTED`: Anonymization job submitted
- `JOB_STARTED`: Job processing started
- `JOB_SUCCEEDED`: Job completed successfully
- `JOB_FAILED`: Job failed with error
- `JOB_CANCELLED`: Job cancelled
- `RESULTS_DOWNLOADED`: Results downloaded by user

---

### GET `/audit/user/{userId}`
**Description**: Get audit logs for a specific user (AUDITOR/ADMIN only)

**Authentication**: Required (AUDITOR, ADMIN)

**Response (200 OK)**:
```json
{
  "userId": 2,
  "username": "analyst",
  "totalActions": 6,
  "logs": [...]
}
```

---

### GET `/audit/action/{action}`
**Description**: Get audit logs filtered by action type

**Authentication**: Required (AUDITOR, ADMIN)

**Path Parameters**:
- `action`: Action type (e.g., "DATASET_UPLOADED", "JOB_SUBMITTED")

**Response (200 OK)**:
```json
{
  "action": "DATASET_UPLOADED",
  "totalCount": 5,
  "logs": [...]
}
```

---

### GET `/audit/export`
**Description**: Export audit logs as CSV (ADMIN only)

**Authentication**: Required (ADMIN)

**Query Parameters**:
- `startDate` (optional): Start date
- `endDate` (optional): End date
- `action` (optional): Filter by action

**Response (200 OK)**:
```
Content-Type: text/csv
Content-Disposition: attachment; filename="audit_logs_2026-05-25.csv"

[CSV file content]
```

---

## 🔑 Role-Based Access Control

| Endpoint | Public | ANALYST | AUDITOR | ADMIN |
|----------|--------|---------|---------|-------|
| POST /auth/login | ✅ | ✅ | ✅ | ✅ |
| GET /auth/health | ✅ | ✅ | ✅ | ✅ |
| POST /datasets/upload | ❌ | ✅ | ❌ | ✅ |
| GET /datasets | ❌ | ✅ | ✅ | ✅ |
| GET /datasets/{id} | ❌ | ✅ | ✅ | ✅ |
| DELETE /datasets/{id} | ❌ | ✅* | ❌ | ✅ |
| POST /policies | ❌ | ✅ | ❌ | ✅ |
| GET /policies | ❌ | ✅ | ✅ | ✅ |
| PUT /policies/{id} | ❌ | ✅* | ❌ | ✅ |
| DELETE /policies/{id} | ❌ | ✅* | ❌ | ✅ |
| POST /jobs | ❌ | ✅ | ❌ | ✅ |
| GET /jobs | ❌ | ✅ | ✅ | ✅ |
| GET /jobs/{id}/download | ❌ | ✅ | ✅ | ✅ |
| GET /jobs/status/queued | ❌ | ❌ | ❌ | ✅ |
| GET /audit | ❌ | ✅** | ✅ | ✅ |
| GET /audit/export | ❌ | ❌ | ✅ | ✅ |

*Owner only
**Own logs only

---

## 🚨 Error Codes

| Code | Message | Meaning |
|------|---------|---------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Request successful, no response body |
| 400 | Bad Request | Invalid input or request |
| 401 | Unauthorized | Authentication required or failed |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists or conflict |
| 413 | Payload Too Large | File exceeds size limit |
| 500 | Internal Server Error | Server error |

---

## 🧪 Testing API with Postman/cURL

### 1. Login
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"analyst","password":"analyst123"}' \
  | jq -r '.token')

echo "Token: $TOKEN"
```

### 2. Upload Dataset
```bash
curl -X POST http://localhost:8080/api/datasets/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@sample-data/customers.csv"
```

### 3. Create Policy
```bash
curl -X POST http://localhost:8080/api/policies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "datasetId": 1,
    "name": "Test_Policy",
    "rules": [
      {"columnName": "email", "strategy": "HASH"},
      {"columnName": "phone", "strategy": "MASK", "parameters": {"showLastN": 4}}
    ]
  }'
```

### 4. Submit Job
```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"datasetId": 1, "policyId": 1}'
```

### 5. Check Job Status (Poll)
```bash
for i in {1..10}; do
  curl -s -X GET http://localhost:8080/api/jobs/1 \
    -H "Authorization: Bearer $TOKEN" | jq '.status'
  sleep 2
done
```

---

## 📚 API Documentation

Full API documentation is available at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

---

Last Updated: 2026-05-25
