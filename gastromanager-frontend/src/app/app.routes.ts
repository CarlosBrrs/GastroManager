import {Routes} from '@angular/router';
import {LayoutComponent} from "./core/layout/layout.component";
import {authGuard} from "./core/guards/auth.guard";
import {InventoryFormComponent} from "./pages/inventory/inventory-form/inventory-form.component";

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: '',
    component: LayoutComponent,
    children: [
      {path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)},
      {
        path: 'home',
        loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
        canActivate: [authGuard]
      },
      {
        path: 'inventory',
        loadComponent: () => import('./pages/inventory/inventory.component').then(m => m.InventoryComponent),
        canActivate: [authGuard],
        children: [
          {
            path: 'new',
            component: InventoryFormComponent,
            outlet: 'sidebar'
          }
        ]
      },
      {
        path: 'product-items',
        loadComponent: () => import('./pages/product-item/product-item.component').then(m => m.ProductItemComponent),
        canActivate: [authGuard]
      },
      {
        path: 'orders',
        children: [
          {
            path: '',
            loadComponent: () => import('./pages/order/order.component').then(m => m.OrderComponent),
            canActivate: [authGuard]
          },
          {
            path: 'create-order',
            loadComponent: () => import('./pages/order/create-order/create-order.component').then(m => m.CreateOrderComponent),
            canActivate: [authGuard]
          }
        ]
      },
    ]
  }
];
