import {Routes} from "@angular/router";

export const managementRoutes: Routes = [
    {
      path: 'management',
      loadComponent: () => import('./infrastructure/ui/management-page/management-page.component')
        .then(m => m.ManagementPageComponent)
    },
    {
      path: 'management/cash-registers',
      loadComponent: () => import('./infrastructure/ui/cash-registers/management-cash-registers-page.component')
        .then(m => m.ManagementCashRegistersPageComponent)
    },
    {
      path: '',
      pathMatch:
        'full',
      redirectTo:
        'management'
    },
  ];
