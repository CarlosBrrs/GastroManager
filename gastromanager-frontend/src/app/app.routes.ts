import {Routes} from '@angular/router';
import {PublicLayoutComponent} from "./layouts/public-layout/public-layout.component";
import {AuthenticatedLayoutComponent} from "./layouts/authenticated-layout/component/authenticated-layout.component";
import {authGuard} from "./core/guards/auth.guard";
import {authRoutes} from "./features/auth/auth.routes";
import {dashboardRoutes} from "./features/dashboard/dashboard.routes";
import {ingredientRoutes} from "./features/ingredients/ingredient.routes";
import {menuRoutes} from './features/menus/menu.routes';
import {submenuRoutes} from "./features/submenus/submenus.routes";
import {productRoutes} from "./features/products/products.routes";
import {orderRoutes} from "./features/orders/order.routes";
import {managementRoutes} from "./features/management/management.routes";
import {paymentsRoutes} from "./features/payments/payments.routes";
import {reportsRoutes} from "./features/reports/reports.routes";

/*export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: 'register',
        loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
      },
      {
        path: 'verify-pending',
        loadComponent: () => import('./pages/verify-pending/verify-pending.component').then(m => m.VerifyPendingComponent)
      },
      {
        path: 'verify-account',
        loadComponent: () => import('./pages/verify-account/verify-account.component').then(m => m.VerifyAccountComponent)
      },
      {path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)},
      {
        path: 'home',
        loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
        canActivate: [xauthxGuard]
      },
      {
        path: 'inventory',
        loadComponent: () => import('./pages/inventory/inventory.component').then(m => m.InventoryComponent),
        canActivate: [xauthxGuard],
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
        canActivate: [xauthxGuard]
      },
      {
        path: 'orders',
        children: [
          {
            path: '',
            loadComponent: () => import('./pages/orders/orders.component').then(m => m.OrderComponent),
            canActivate: [xauthxGuard]
          },
          {
            path: 'create-orders',
            loadComponent: () => import('./pages/orders/create-orders/create-orders.component').then(m => m.CreateOrderPageComponent),
            canActivate: [xauthxGuard]
          }
        ]
      },
    ]
  }
];*/

export const routes2: Routes = [
  {
    path: '',
    component: AuthenticatedLayoutComponent,
    canActivate: [authGuard],
    children: [
      ...dashboardRoutes,
      ...ingredientRoutes,
      ...menuRoutes,
      ...submenuRoutes,
      ...productRoutes,
      ...orderRoutes,
      ...managementRoutes,
      ...paymentsRoutes,
      ...reportsRoutes
    ]
  },
  {
    path: '',
    component: PublicLayoutComponent,
    children: [
      ...authRoutes
    ]
  },
  {path: '**', redirectTo: ''}
];
