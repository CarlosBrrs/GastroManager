import {Routes} from "@angular/router";

export const menuRoutes: Routes = [
  {
    path: 'menus',
    loadComponent: () => import('./infrastructure/ui/menus-page/menus-page.component')
      .then(m => m.MenusPageComponent),
  },
  {
    path: 'menus/create',
    loadComponent: () => import('./infrastructure/ui/create-menu-page/create-update-menus-page.component')
      .then(m => m.CreateUpdateMenusPageComponent)
  },
  {
    path: 'menus/:uuid/edit',
    loadComponent: () => import('./infrastructure/ui/create-menu-page/create-update-menus-page.component')
      .then(m => m.CreateUpdateMenusPageComponent)
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'menus'
  },
];
