import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map, switchMap, tap } from 'rxjs';

import { environment } from '../../../environments/environment';

interface RegisterResponse {
  email: string;
  role: string;
}

export interface Profile {
  email: string;
  firstName: string | null;
  lastName: string | null;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly storageKey = 'ecommerce.basic-auth';
  private readonly credentials = signal(this.readCredentials());

  readonly email = signal(this.credentials()?.email ?? '');
  readonly isAuthenticated = signal(Boolean(this.credentials()));

  register(email: string, password: string): Observable<void> {
    return this.http.post<RegisterResponse>(`${environment.apiUrl}/v1/auth/register`, { email, password })
      .pipe(switchMap(() => this.login(email, password)));
  }

  login(email: string, password: string): Observable<void> {
    const headers = this.basicHeaders(email, password);
    return this.http.get(`${environment.apiUrl}/v1/cart`, { headers }).pipe(
      tap(() => {
        const credentials = { email, password };
        sessionStorage.setItem(this.storageKey, JSON.stringify(credentials));
        this.credentials.set(credentials);
        this.email.set(email);
        this.isAuthenticated.set(true);
      }),
      map(() => undefined)
    );
  }

  logout(): void {
    sessionStorage.removeItem(this.storageKey);
    this.credentials.set(null);
    this.email.set('');
    this.isAuthenticated.set(false);
  }

  getAuthHeaders(): HttpHeaders {
    const credentials = this.credentials();
    return credentials
      ? this.basicHeaders(credentials.email, credentials.password)
      : new HttpHeaders();
  }

  getProfile(): Observable<Profile> {
    return this.http.get<Profile>(`${environment.apiUrl}/v1/me`, {
      headers: this.getAuthHeaders()
    });
  }

  updateProfile(firstName: string, lastName: string): Observable<Profile> {
    return this.http.put<Profile>(`${environment.apiUrl}/v1/me`, { firstName, lastName }, {
      headers: this.getAuthHeaders()
    });
  }

  private basicHeaders(email: string, password: string): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Basic ${btoa(`${email}:${password}`)}`
    });
  }

  private readCredentials(): { email: string; password: string } | null {
    const stored = sessionStorage.getItem(this.storageKey);
    if (!stored) {
      return null;
    }

    try {
      return JSON.parse(stored) as { email: string; password: string };
    } catch {
      sessionStorage.removeItem(this.storageKey);
      return null;
    }
  }
}
