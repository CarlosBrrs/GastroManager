import {Routes} from '@angular/router';
import {LayoutComponent} from "./core/layout/layout.component";
import {LoginComponent} from "./pages/login/login.component";
import {HomeComponent} from "./pages/home/home.component";
import {authGuard} from "./core/guards/auth.guard";

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
      {path: 'login', component: LoginComponent},
      {path: 'home', component: HomeComponent, canActivate: [authGuard]}
    ]
  }
];
