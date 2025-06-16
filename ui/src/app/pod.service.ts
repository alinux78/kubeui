import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Pod {
  name: string;
  namespace: string;
  status: string;
  age: string
}

@Injectable({
  providedIn: 'root'
})
export class PodService {


  private apiUrl = 'http://localhost:8080/pods';

  constructor(private http: HttpClient) {}

  getPods(): Observable<Pod[]> {
    return this.http.get<Pod[]>(this.apiUrl);
  }

  restartPod(pod: Pod, onSuccess?: () => void): void {
    const restartUrl = `${this.apiUrl}/${pod.namespace}/${pod.name}/restart`;
    this.http.post(restartUrl, {}).subscribe({
      next: () => onSuccess ? onSuccess() : "",
      error: (err) => console.error('Failed to restart pod', err)
    });
  }

    desscribePod(pod: Pod): Observable<object> {
      const restartUrl = `${this.apiUrl}/${pod.namespace}/${pod.name}/describe`;
      return this.http.get(restartUrl, {});
  }
}
