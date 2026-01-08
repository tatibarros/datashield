export interface User {
  id: number;
  username: string;
  email: string;
  role: 'ADMIN' | 'ANALYST' | 'AUDITOR';
}

export interface Dataset {
  id: number;
  name: string;
  fileType: string;
  rowCount: number;
  columnCount: number;
  columns: string[];
  ownerUsername: string;
  createdAt: string;
}

export interface AnonymizationPolicy {
  id: number;
  name: string;
  datasetId: number;
  rules: any;
  version: number;
  active: boolean;
  createdAt: string;
}

export interface AnonymizationJob {
  id: number;
  datasetId: number;
  policyId: number;
  status: 'QUEUED' | 'RUNNING' | 'SUCCEEDED' | 'FAILED';
  processedRows: number;
  errorMessage: string;
  createdAt: string;
  completedAt: string;
}

export interface AuditLog {
  id: number;
  userId: number;
  action: string;
  resource: string;
  resourceId: number;
  details: string;
  ipAddress: string;
  createdAt: string;
}
