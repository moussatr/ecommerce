import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

import { HealthService } from './core/services/health.service';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterLink, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private readonly healthService = inject(HealthService);
  readonly authService = inject(AuthService);

  readonly status = signal('Checking...');

  constructor() {
    this.healthService.getHealth().subscribe({
      next: (response) => this.status.set(response.status),
      error: () => this.status.set('API unavailable')
    });
  }
}
