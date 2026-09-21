import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { HealthService } from './core/services/health.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private readonly healthService = inject(HealthService);

  readonly status = signal('Checking...');

  constructor() {
    this.healthService.getHealth().subscribe({
      next: (response) => this.status.set(response.status),
      error: () => this.status.set('API unavailable')
    });
  }
}
