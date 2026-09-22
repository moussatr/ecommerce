import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <section class="auth">
      <a routerLink="/catalog">Retour au catalogue</a>
      <p class="eyebrow">Compte client</p>
      <h2>{{ registering ? 'Créer un compte' : 'Se connecter' }}</h2>
      <p class="hint">Les nouveaux comptes sont créés avec le rôle client.</p>

      <form (ngSubmit)="submit()">
        <label>
          Email
          <input type="email" name="email" [(ngModel)]="email" required autocomplete="email" />
        </label>
        <label>
          Mot de passe
          <input type="password" name="password" [(ngModel)]="password" required minlength="8" autocomplete="current-password" />
        </label>
        @if (error) {
          <p class="error">{{ error }}</p>
        }
        <button type="submit" [disabled]="submitting">
          {{ submitting ? 'Traitement...' : (registering ? 'Créer le compte' : 'Se connecter') }}
        </button>
      </form>

      <button class="switch" type="button" (click)="registering = !registering">
        {{ registering ? 'J’ai déjà un compte' : 'Créer un compte' }}
      </button>
    </section>
  `,
  styles: [`
    .auth { width: min(100%, 28rem); margin: 0 auto; display: grid; gap: 1rem; }
    .auth > a, .switch { color: #176b87; font-weight: 600; text-decoration: none; }
    .eyebrow { margin: 1.5rem 0 0; color: #176b87; text-transform: uppercase; letter-spacing: .08em; }
    h2, p { margin: 0; }
    h2 { color: #18212f; font-size: 2rem; }
    .hint { color: #697386; }
    form { display: grid; gap: 1rem; padding: 1.5rem; border: 1px solid #e1e6ed; border-radius: .5rem; background: #fff; }
    label { display: grid; gap: .4rem; color: #526071; font-weight: 600; }
    input { min-height: 2.75rem; padding: .5rem .7rem; border: 1px solid #d6dce5; border-radius: .35rem; font: inherit; }
    form button { border: 0; border-radius: .35rem; padding: .7rem; background: #176b87; color: #fff; font: inherit; font-weight: 600; }
    button { cursor: pointer; }
    button:disabled { opacity: .6; cursor: wait; }
    .switch { border: 0; background: transparent; font: inherit; text-align: left; }
    .error { color: #b42318; }
  `]
})
export class AuthComponent {
  private readonly authService = inject(AuthService);

  email = '';
  password = '';
  registering = false;
  submitting = false;
  error = '';

  submit(): void {
    this.submitting = true;
    this.error = '';
    const request = this.registering
      ? this.authService.register(this.email, this.password)
      : this.authService.login(this.email, this.password);

    request.subscribe({
      next: () => this.submitting = false,
      error: () => {
        this.submitting = false;
        this.error = this.registering
          ? 'Impossible de créer le compte.'
          : 'Email ou mot de passe incorrect.';
      }
    });
  }
}
