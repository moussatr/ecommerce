import { DecimalPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

import { AuthService } from './core/services/auth.service';
import { CartService } from './features/cart/cart.service';

@Component({
  selector: 'app-root',
  imports: [DecimalPipe, RouterLink, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  readonly authService = inject(AuthService);
  readonly cartService = inject(CartService);
  readonly cartOpen = signal(false);

  toggleCart(): void {
    this.cartOpen.update((isOpen) => !isOpen);
  }

  closeCart(): void {
    this.cartOpen.set(false);
  }
}
