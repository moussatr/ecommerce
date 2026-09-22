import { Component, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { CartService } from './cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [DecimalPipe, RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent {
  readonly cartService = inject(CartService);

  decrease(productId: number, quantity: number): void {
    this.cartService.setQuantity(productId, quantity - 1);
  }

  increase(productId: number, quantity: number): void {
    this.cartService.setQuantity(productId, quantity + 1);
  }
}
