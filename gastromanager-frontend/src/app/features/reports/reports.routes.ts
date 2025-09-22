// Rutas para reports
import { Routes } from '@angular/router';

export const reportsRoutes: Routes = [
  {
    path: 'reports',
    loadComponent: () => import('./infrastructure/ui/reports-page/reports-page.component').then(m => m.ReportsPageComponent)
  }
];
