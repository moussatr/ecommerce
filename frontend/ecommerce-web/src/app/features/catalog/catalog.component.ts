import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { CatalogService } from './catalog.service';
import { CartService } from '../cart/cart.service';
import { Category, Product } from '../../shared/models/product.model';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [DecimalPipe, RouterLink],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.scss'
})
export class CatalogComponent {
  private readonly catalogService = inject(CatalogService);
  private readonly cartService = inject(CartService);

  readonly products = signal<Product[]>([]);
  readonly categories = signal<Category[]>([]);
  readonly loading = signal(true);
  readonly error = signal('');
  searchTerm = '';
  selectedCategoryId: number | null = null;

  constructor() {
    this.catalogService.getCategories().subscribe({
      next: (categories) => this.categories.set(categories),
      error: () => this.error.set('Impossible de charger les catégories')
    });

    this.loadProducts();
  }

  loadProducts(): void {
    this.loading.set(true);
    this.error.set('');
    this.catalogService.getProducts(0, 20, this.searchTerm, this.selectedCategoryId).subscribe({
      next: (page) => {
        this.products.set(page.content);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger les produits');
        this.loading.set(false);
      }
    });
  }

  onSearch(event: Event): void {
    this.searchTerm = (event.target as HTMLInputElement).value;
    this.loadProducts();
  }

  onCategoryChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedCategoryId = value ? Number(value) : null;
    this.loadProducts();
  }

  addToCart(product: Product): void {
    this.cartService.add(product);
  }
}
