import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JobService } from '../services/job.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-jobs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container mt-4">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Jobs</h2>
        <a routerLink="/dashboard" class="btn btn-secondary">Back</a>
      </div>

      <div class="card mb-3">
        <div class="card-body">
          <h5>Start Job</h5>
          <form (submit)="start()" class="row g-2 align-items-center">
            <div class="col-auto">
              <input type="number" class="form-control" placeholder="Dataset ID" [(ngModel)]="datasetId" name="datasetId" required>
            </div>
            <div class="col-auto">
              <input type="number" class="form-control" placeholder="Policy ID" [(ngModel)]="policyId" name="policyId" required>
            </div>
            <div class="col-auto">
              <button class="btn btn-primary">Start</button>
            </div>
          </form>
          <div *ngIf="message" class="mt-2 alert alert-info">{{ message }}</div>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <h5>Jobs for Dataset</h5>
          <div class="mb-2">
            <input type="number" class="form-control d-inline-block w-auto" placeholder="Dataset ID" [(ngModel)]="listDatasetId">
            <button class="btn btn-sm btn-outline-primary ms-2" (click)="loadJobs()">Load</button>
          </div>

          <table class="table table-sm" *ngIf="jobs?.length">
            <thead>
              <tr>
                <th>ID</th>
                <th>Status</th>
                <th>Started</th>
                <th>Completed</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let j of jobs">
                <td>{{ j.id }}</td>
                <td>{{ j.status }}</td>
                <td>{{ j.startedAt | date:'short' }}</td>
                <td>{{ j.completedAt | date:'short' }}</td>
              </tr>
            </tbody>
          </table>
          <div *ngIf="jobs && jobs.length === 0" class="text-muted">No jobs found</div>
        </div>
      </div>
    </div>
  `
})
export class JobsComponent {
  datasetId?: number;
  policyId?: number;
  listDatasetId?: number;
  jobs: any[] = [];
  message?: string;

  constructor(private jobService: JobService, private router: Router) {}

  start() {
    if (!this.datasetId || !this.policyId) { return; }
    this.jobService.startJob(this.datasetId, this.policyId).subscribe({
      next: (res) => this.message = `Job queued (id=${res.id})`,
      error: (err) => this.message = `Error: ${err.error?.message || err.message}`
    });
  }

  loadJobs() {
    if (!this.listDatasetId) { return; }
    this.jobService.listJobsForDataset(this.listDatasetId).subscribe({
      next: (data) => this.jobs = data,
      error: (err) => console.error(err)
    });
  }
}