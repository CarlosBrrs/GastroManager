// Rutas para payments
import { Routes } from '@angular/router';

export const paymentsRoutes: Routes = [
  {
    path: 'payments',
    loadComponent: () => import('./infrastructure/ui/payments-form-page/payments-form-page.component').then(c => c.PaymentsFormPageComponent)
  }
];
