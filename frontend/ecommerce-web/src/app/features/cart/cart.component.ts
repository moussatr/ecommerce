import { Component, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

import { CartService } from './cart.service';
import { AuthService } from '../../core/services/auth.service';
import { OrderService } from '../order/order.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [DecimalPipe, RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent {
  readonly cartService = inject(CartService);
  private readonly authService = inject(AuthService);
  private readonly orderService = inject(OrderService);
  private readonly router = inject(Router);
  ordering = false;
  orderError = '';

  decrease(productId: number, quantity: number): void {
    this.cartService.setQuantity(productId, quantity - 1);
  }

  increase(productId: number, quantity: number): void {
    this.cartService.setQuantity(productId, quantity + 1);
  }

  placeOrder(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/auth']);
      return;
    }
    this.ordering = true;
    this.orderError = '';
    this.orderService.createOrder().subscribe({
      next: () => {
        this.cartService.refresh();
        this.ordering = false;
        this.router.navigate(['/orders']);
      },
      error: () => {
        this.orderError = 'La commande ne peut pas être créée. Vérifiez le stock.';
        this.ordering = false;
      }
    });
  }
}
