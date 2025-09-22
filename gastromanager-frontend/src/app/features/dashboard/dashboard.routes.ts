import {Routes} from "@angular/router";

export const dashboardRoutes: Routes = [
  {
    path: 'dashboard',
    loadComponent: () => import('./infrastructure/ui/dashboard-page/dashboard-page.component').then(m => m.DashboardPageComponent),
    data: {showSecondarySidebar: false},
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard'
  },
];
