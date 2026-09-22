import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';

import { CatalogService } from './catalog.service';
import { CartService } from '../cart/cart.service';
import { Product } from '../../shared/models/product.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [DecimalPipe, RouterLink],
  template: `
    <section class="detail">
      <a routerLink="/catalog">Retour au catalogue</a>
      @if (loading()) {
        <p>Chargement du produit...</p>
      } @else if (error()) {
        <p class="error">{{ error() }}</p>
      } @else {
        @if (product(); as item) {
          <p class="eyebrow">{{ item.category?.name ?? 'Sans catégorie' }}</p>
          <h2>{{ item.name }}</h2>
          <p>{{ item.description || 'Aucune description disponible.' }}</p>
          <strong>{{ item.price | number: '1.2-2' }} €</strong>
          <span>{{ item.stock }} en stock</span>
          <button type="button" [disabled]="item.stock < 1" (click)="cartService.add(item)">
            {{ item.stock < 1 ? 'Rupture de stock' : 'Ajouter au panier' }}
          </button>
        }
      }
    </section>
  `,
  styles: [`
    .detail { max-width: 52rem; margin: 0 auto; display: grid; gap: 1rem; }
    .detail a { color: #176b87; font-weight: 600; text-decoration: none; }
    .eyebrow { color: #176b87; text-transform: uppercase; letter-spacing: .08em; }
    h2 { color: #18212f; font-size: clamp(2rem, 5vw, 3.5rem); margin: 1rem 0 0; }
    strong { color: #c24f32; font-size: 1.5rem; }
    span { color: #697386; }
    .error { color: #b42318; }
    button { border: 0; border-radius: .35rem; padding: .7rem 1rem; background: #176b87; color: #fff; font: inherit; font-weight: 600; cursor: pointer; }
    button:disabled { background: #d6dce5; color: #697386; cursor: not-allowed; }
  `]
})
export class ProductDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly catalogService = inject(CatalogService);
  readonly cartService = inject(CartService);

  readonly product = signal<Product | null>(null);
  readonly loading = signal(true);
  readonly error = signal('');

  constructor() {
    this.route.paramMap.pipe(
      switchMap((params) => this.catalogService.getProduct(Number(params.get('id'))))
    ).subscribe({
      next: (product) => {
        this.product.set(product);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Produit introuvable');
        this.loading.set(false);
      }
    });
  }
}