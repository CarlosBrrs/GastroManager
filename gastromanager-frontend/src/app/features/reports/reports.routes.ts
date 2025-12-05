// Rutas para reports
import {Routes} from '@angular/router';

export const reportsRoutes: Routes = [
  {
    path: 'reports',
    loadComponent: () => import('./infrastructure/ui/reports-dashboard/reports-dashboard.component').then(m => m.ReportsDashboardComponent)
  },
  {
    path: 'reports/sales',
    loadComponent: () => import('./infrastructure/ui/reports-page/sales-reports-page.component').then(m => m.SalesReportsPageComponent)
  },
  {
    path: 'reports/inventory',
    loadComponent: () => import('./infrastructure/ui/reports-dashboard/reports-dashboard.component').then(m => m.ReportsDashboardComponent)
  },
  {
    path: 'reports/customers',
    loadComponent: () => import('./infrastructure/ui/reports-dashboard/reports-dashboard.component').then(m => m.ReportsDashboardComponent)
  },
  {
    path: 'reports/performance',
    loadComponent: () => import('./infrastructure/ui/reports-dashboard/reports-dashboard.component').then(m => m.ReportsDashboardComponent)
  }
];
