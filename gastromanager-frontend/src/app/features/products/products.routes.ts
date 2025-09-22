import {Routes} from "@angular/router";

export const productRoutes: Routes = [
  {
    path: 'products',
    loadComponent: () => import('./infrastructure/ui/products-page/products-page.component')
      .then(m => m.ProductsPageComponent),
  },
  {
    path: 'products/create',
    loadComponent: () => import('./infrastructure/ui/create-product-page/create-update-products-page.component')
      .then(m => m.CreateUpdateProductsPageComponent)
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'products'
  },
];
