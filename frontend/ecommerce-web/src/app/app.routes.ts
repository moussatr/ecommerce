import { Routes } from '@angular/router';
import { CatalogComponent } from './features/catalog/catalog.component';
import { ProductDetailComponent } from './features/catalog/product-detail.component';
import { CartComponent } from './features/cart/cart.component';
import { AuthComponent } from './features/auth/auth.component';
import { ProfileComponent } from './features/profile/profile.component';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'catalog' },
	{ path: 'catalog', component: CatalogComponent },
	{ path: 'catalog/products/:id', component: ProductDetailComponent },
	{ path: 'cart', component: CartComponent },
	{ path: 'auth', component: AuthComponent }
	, { path: 'profile', component: ProfileComponent }
];
