import { Component, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { Order, OrderService } from './order.service';
import { PaymentService } from './payment.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [DatePipe, DecimalPipe, RouterLink],
  template: `
    <section class="orders">
      <a routerLink="/catalog">Retour au catalogue</a>
      <p class="eyebrow">Espace client</p>
      <h2>Mes commandes</h2>
      @if (loading()) {
        <p>Chargement des commandes...</p>
      } @else if (error()) {
        <p class="error">{{ error() }}</p>
      } @else if (!orders().length) {
        <p class="empty">Aucune commande pour le moment.</p>
      } @else {
        @for (order of orders(); track order.id) {
          <article class="order">
            <header>
              <div>
                <h3>Commande #{{ order.id }}</h3>
                <p>{{ order.createdAt | date: 'medium' }}</p>
              </div>
              <span>{{ order.status }}</span>
            </header>
            @for (item of order.items; track item.productId) {
              <p>{{ item.productName }} × {{ item.quantity }}</p>
            }
            <strong>{{ order.totalAmount | number: '1.2-2' }} €</strong>
            @if (order.status === 'PENDING') {
              <button type="button" (click)="pay(order.id)" [disabled]="payingOrderId === order.id">
                {{ payingOrderId === order.id ? 'Redirection...' : 'Payer la commande' }}
              </button>
            }
          </article>
        }
      }
    </section>
  `,
  styles: [`
    .orders { width: min(100%, 52rem); margin: 0 auto; display: grid; gap: 1rem; }
    .orders > a { color: #176b87; font-weight: 600; text-decoration: none; }
    .eyebrow { margin: 1.5rem 0 0; color: #176b87; text-transform: uppercase; letter-spacing: .08em; }
    h2, h3, p { margin: 0; }
    h2 { color: #18212f; font-size: 2.5rem; }
    .order { display: grid; gap: .7rem; padding: 1.25rem; border: 1px solid #e1e6ed; border-radius: .5rem; background: #fff; }
    .order header { display: flex; justify-content: space-between; gap: 1rem; }
    .order header p, .order > p { color: #697386; }
    .order header span { color: #176b87; font-weight: 600; }
    .order strong { color: #c24f32; font-size: 1.2rem; }
    .order button { width: fit-content; border: 0; border-radius: .35rem; padding: .65rem .8rem; background: #176b87; color: #fff; font: inherit; font-weight: 600; cursor: pointer; }
    .order button:disabled { opacity: .6; cursor: wait; }
    .error { color: #b42318; }
  `]
})
export class OrdersComponent {
  private readonly orderService = inject(OrderService);
  private readonly paymentService = inject(PaymentService);

  readonly orders = signal<Order[]>([]);
  readonly loading = signal(true);
  readonly error = signal('');
  payingOrderId: number | null = null;

  constructor() {
    this.orderService.getOrders().subscribe({
      next: (orders) => {
        this.orders.set(orders);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger les commandes.');
        this.loading.set(false);
      }
    });
  }

  pay(orderId: number): void {
    this.payingOrderId = orderId;
    this.error.set('');
    this.paymentService.createCheckout(orderId).subscribe({
      next: (checkout) => window.location.assign(checkout.checkoutUrl),
      error: () => {
        this.error.set('Paiement indisponible. Configurez Stripe puis réessayez.');
        this.payingOrderId = null;
      }
    });
  }
}
