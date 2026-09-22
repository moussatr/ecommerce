import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Product } from '../../shared/models/product.model';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../core/services/auth.service';

export interface CartItem {
  product: Product;
  quantity: number;
}

interface CartResponse {
  items: CartItem[];
  total: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);
  private readonly apiUrl = `${environment.apiUrl}/v1/cart`;
  private readonly items = signal<CartItem[]>([]);

  readonly cartItems = this.items.asReadonly();
  readonly itemCount = computed(() => this.items().reduce((total, item) => total + item.quantity, 0));
  readonly total = computed(() => this.items().reduce(
    (total, item) => total + item.product.price * item.quantity,
    0
  ));

  constructor() {
    if (this.authService.isAuthenticated()) {
      this.loadRemoteCart();
    }
  }

  add(product: Product): void {
    if (product.stock < 1) {
      return;
    }

    if (this.authService.isAuthenticated()) {
      this.http.post<CartResponse>(`${this.apiUrl}/items`, {
        productId: product.id,
        quantity: 1
      }, { headers: this.authService.getAuthHeaders() }).subscribe({
        next: (cart) => this.replaceItems(cart.items)
      });
      return;
    }

    const currentItems = this.items();
    const existingItem = currentItems.find((item) => item.product.id === product.id);

    if (existingItem) {
      this.items.set(currentItems.map((item) => item.product.id === product.id
        ? { ...item, quantity: Math.min(item.quantity + 1, product.stock) }
        : item));
      return;
    }

    this.items.set([...currentItems, { product, quantity: 1 }]);
  }

  setQuantity(productId: number, quantity: number): void {
    const item = this.items().find((cartItem) => cartItem.product.id === productId);
    if (!item) {
      return;
    }

    const safeQuantity = Math.max(0, Math.min(quantity, item.product.stock));
    if (this.authService.isAuthenticated()) {
      if (safeQuantity === 0) {
        this.remove(productId);
        return;
      }
      this.http.put<CartResponse>(`${this.apiUrl}/items/${productId}`, {
        productId,
        quantity: safeQuantity
      }, { headers: this.authService.getAuthHeaders() }).subscribe({
        next: (cart) => this.replaceItems(cart.items)
      });
      return;
    }

    this.items.set(safeQuantity === 0
      ? this.items().filter((cartItem) => cartItem.product.id !== productId)
      : this.items().map((cartItem) => cartItem.product.id === productId
        ? { ...cartItem, quantity: safeQuantity }
        : cartItem));
  }

  remove(productId: number): void {
    if (this.authService.isAuthenticated()) {
      this.http.delete<CartResponse>(`${this.apiUrl}/items/${productId}`, {
        headers: this.authService.getAuthHeaders()
      }).subscribe({
        next: (cart) => this.replaceItems(cart.items)
      });
      return;
    }
    this.items.set(this.items().filter((item) => item.product.id !== productId));
  }

  private loadRemoteCart(): void {
    this.http.get<CartResponse>(this.apiUrl, {
      headers: this.authService.getAuthHeaders()
    }).subscribe({
      next: (cart) => this.replaceItems(cart.items),
      error: () => this.authService.logout()
    });
  }

  private replaceItems(items: CartItem[]): void {
    this.items.set(items);
  }
}
