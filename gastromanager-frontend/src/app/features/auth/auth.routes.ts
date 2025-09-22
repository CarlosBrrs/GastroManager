import {Routes} from "@angular/router";

export const authRoutes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./infrastructure/ui/login-page/login-page.component').then(m => m.LoginPageComponent),
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login'
  }
];
