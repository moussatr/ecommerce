import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { AuthService, Profile } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <section class="profile">
      <a routerLink="/catalog">Retour au catalogue</a>
      <p class="eyebrow">Mon compte</p>
      <h2>Mon profil</h2>

      @if (loading) {
        <p>Chargement du profil...</p>
      } @else {
        <p class="email">{{ profile?.email }}</p>
        <form (ngSubmit)="save()">
          <label>
            Prénom
            <input name="firstName" [(ngModel)]="firstName" required />
          </label>
          <label>
            Nom
            <input name="lastName" [(ngModel)]="lastName" required />
          </label>
          @if (message) { <p class="success">{{ message }}</p> }
          @if (error) { <p class="error">{{ error }}</p> }
          <button type="submit" [disabled]="saving">{{ saving ? 'Enregistrement...' : 'Enregistrer' }}</button>
        </form>
      }
    </section>
  `,
  styles: [`
    .profile { width: min(100%, 28rem); margin: 0 auto; display: grid; gap: 1rem; }
    .profile > a { color: #176b87; font-weight: 600; text-decoration: none; }
    .eyebrow { margin: 1.5rem 0 0; color: #176b87; text-transform: uppercase; letter-spacing: .08em; }
    h2, p { margin: 0; }
    h2 { color: #18212f; font-size: 2rem; }
    .email { color: #697386; }
    form { display: grid; gap: 1rem; padding: 1.5rem; border: 1px solid #e1e6ed; border-radius: .5rem; background: #fff; }
    label { display: grid; gap: .4rem; color: #526071; font-weight: 600; }
    input { min-height: 2.75rem; padding: .5rem .7rem; border: 1px solid #d6dce5; border-radius: .35rem; font: inherit; }
    button { border: 0; border-radius: .35rem; padding: .7rem; background: #176b87; color: #fff; font: inherit; font-weight: 600; cursor: pointer; }
    button:disabled { opacity: .6; cursor: wait; }
    .success { color: #087443; }
    .error { color: #b42318; }
  `]
})
export class ProfileComponent {
  private readonly authService = inject(AuthService);

  profile: Profile | null = null;
  firstName = '';
  lastName = '';
  loading = true;
  saving = false;
  message = '';
  error = '';

  constructor() {
    this.authService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.firstName = profile.firstName ?? '';
        this.lastName = profile.lastName ?? '';
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger le profil.';
        this.loading = false;
      }
    });
  }

  save(): void {
    this.saving = true;
    this.message = '';
    this.error = '';
    this.authService.updateProfile(this.firstName, this.lastName).subscribe({
      next: (profile) => {
        this.profile = profile;
        this.message = 'Profil enregistré.';
        this.saving = false;
      },
      error: () => {
        this.error = 'Impossible d’enregistrer le profil.';
        this.saving = false;
      }
    });
  }
}