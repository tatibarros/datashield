import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiUrl = 'http://localhost:8080/api/jobs';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  startJob(datasetId: number, policyId: number): Observable<any> {
    const params = new HttpParams()
      .set('datasetId', datasetId.toString())
      .set('policyId', policyId.toString());
    return this.http.post(`${this.apiUrl}`, null, { headers: this.getHeaders(), params });
  }

  listJobsForDataset(datasetId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/dataset/${datasetId}`, { headers: this.getHeaders() });
  }

  getJob(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}