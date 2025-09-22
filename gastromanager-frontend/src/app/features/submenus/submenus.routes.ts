import {Routes} from "@angular/router";

export const submenuRoutes: Routes = [
  {
    path: 'menus/:menuUuid/add-submenu',
    loadComponent: () => import('./infrastructure/ui/create-submenu-page/create-update-submenus-page.component')
      .then(m => m.CreateUpdateSubmenusPageComponent)
  },
  // {
  //   path: '',
  //   pathMatch: 'full',
  //   redirectTo: 'menus'
  // },
];
