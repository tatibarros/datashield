import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="container mt-5">
      <div class="row justify-content-center">
        <div class="col-md-4">
          <div class="card">
            <div class="card-header bg-primary text-white">
              <h4>DataShield Login</h4>
            </div>
            <div class="card-body">
              <form (ngSubmit)="login()">
                <div class="mb-3">
                  <label for="username" class="form-label">Username</label>
                  <input type="text" class="form-control" id="username" 
                         [(ngModel)]="username" name="username" required>
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">Password</label>
                  <input type="password" class="form-control" id="password" 
                         [(ngModel)]="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Login</button>
              </form>
              <hr>
              <p class="text-muted small">Demo Credentials:</p>
              <p class="text-muted small">admin / admin123</p>
              <p class="text-muted small">analyst / analyst123</p>
              <p class="text-muted small">auditor / auditor123</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card { box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }
  `]
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (error: any) => {
        alert('Login failed: ' + (error.error?.message || 'Unknown error'));
      }
    });
  }
}
