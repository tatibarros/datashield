import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { DatasetService } from '../../services/dataset.service';
import { Dataset } from '../../models/index';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container-fluid mt-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>DataShield Dashboard</h1>
        <button class="btn btn-danger" (click)="logout()">Logout</button>
      </div>

      <div class="row">
        <div class="col-md-4">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">Datasets</h5>
              <p class="card-text display-4">{{ datasets.length }}</p>
              <a href="/datasets" class="btn btn-sm btn-primary">Manage</a>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">Recent Jobs</h5>
              <p class="card-text display-4">0</p>
              <a href="/jobs" class="btn btn-sm btn-primary">View</a>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">Audit Logs</h5>
              <p class="card-text display-4">0</p>
              <a href="/audit" class="btn btn-sm btn-primary">View</a>
            </div>
          </div>
        </div>
      </div>

      <div class="row mt-4">
        <div class="col-md-12">
          <div class="card">
            <div class="card-header">
              <h5>Recent Datasets</h5>
            </div>
            <div class="card-body">
              <table class="table table-striped">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Rows</th>
                    <th>Columns</th>
                    <th>Created</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let dataset of datasets | slice:0:5">
                    <td>{{ dataset.name }}</td>
                    <td>{{ dataset.rowCount }}</td>
                    <td>{{ dataset.columnCount }}</td>
                    <td>{{ dataset.createdAt | date: 'short' }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card { box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); margin-bottom: 1rem; }
  `]
})
export class DashboardComponent implements OnInit {
  datasets: Dataset[] = [];

  constructor(
    private authService: AuthService,
    private datasetService: DatasetService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDatasets();
  }

  loadDatasets(): void {
    this.datasetService.listDatasets().subscribe({
      next: (data) => {
        this.datasets = data;
      },
      error: (error) => {
        console.error('Error loading datasets', error);
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
