import {Routes} from "@angular/router";

export const orderRoutes: Routes = [
  {
    path: 'orders',
    loadComponent: () => import('./infrastructure/ui/orders-page/orders-page.component')
      .then(m => m.OrdersPageComponent),
  },
  {
    path: 'orders/create',
    loadComponent: () => import('./infrastructure/ui/create-order-page/create-order-page.component')
      .then(m => m.CreateOrderPageComponent),
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'orders'
  },
];

