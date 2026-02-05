import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DatasetService } from '../services/dataset.service';
import { PolicyService } from '../services/policy.service';
import { JobService } from '../services/job.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-datasets',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <div class="container mt-4">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Datasets</h2>
        <a routerLink="/dashboard" class="btn btn-secondary">Back</a>
      </div>

      <div class="card mb-3">
        <div class="card-body">
          <h5>Your datasets</h5>
          <table class="table table-sm mt-2">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Rows</th>
                <th>Columns</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let d of datasets">
                <td>{{ d.id }}</td>
                <td>{{ d.name }}</td>
                <td>{{ d.rowCount }}</td>
                <td>{{ d.columnCount }}</td>
                <td>
                  <div class="d-flex align-items-center">
                    <select class="form-select form-select-sm me-2" [(ngModel)]="selectedPolicyFor[d.id]">
                      <option [ngValue]="null">Select policy</option>
                      <option *ngFor="let p of policies" [value]="p.id">{{ p.name }} (v{{ p.version }})</option>
                    </select>
                    <button class="btn btn-sm btn-primary" (click)="run(d.id)">Run</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div *ngIf="message" class="alert alert-info">{{ message }}</div>
    </div>
  `
})
export class DatasetsComponent implements OnInit {
  datasets: any[] = [];
  policies: any[] = [];
  selectedPolicyFor: { [key: number]: number | null } = {};
  message?: string;

  constructor(
    private datasetService: DatasetService,
    private policyService: PolicyService,
    private jobService: JobService
  ) {}

  ngOnInit(): void {
    this.loadDatasets();
    this.loadPolicies();
  }

  loadDatasets(): void {
    this.datasetService.listDatasets().subscribe({
      next: (data) => this.datasets = data,
      error: (err) => console.error('Error loading datasets', err)
    });
  }

  loadPolicies(): void {
    this.policyService.listPolicies().subscribe({
      next: (data) => this.policies = data,
      error: (err) => console.error('Error loading policies', err)
    });
  }

  run(datasetId: number): void {
    const policyId = this.selectedPolicyFor[datasetId];
    if (!policyId) { this.message = 'Please select a policy'; return; }
    this.message = undefined;
    this.jobService.startJob(datasetId, policyId).subscribe({
      next: (res) => this.message = `Job queued (id=${res.id})`,
      error: (err) => this.message = `Error: ${err?.error?.message || err.message}`
    });
  }
}
