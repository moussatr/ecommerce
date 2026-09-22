import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Category, Product, ProductPage } from '../../shared/models/product.model';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/v1`;

  getProducts(page = 0, size = 20, query = '', categoryId: number | null = null): Observable<ProductPage> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('q', query);

    const filteredParams = categoryId === null
      ? params
      : params.set('categoryId', categoryId);

    return this.http.get<ProductPage>(`${this.apiUrl}/products`, { params: filteredParams });
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/products/${id}`);
  }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`);
  }
}
