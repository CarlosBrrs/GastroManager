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
      path: 'management/recipes',
      loadComponent: () => import('./infrastructure/ui/recipes/management-recipes-page.component')
        .then(m => m.ManagementRecipesPageComponent)
    },
    {
      path: 'management/recipes/create',
      loadComponent: () => import('./infrastructure/ui/recipes/create-recipe-page/create-recipe-page.component')
        .then(m => m.CreateRecipePageComponent)
    },
    {
      path: '',
      pathMatch: 'full',
      redirectTo: 'management'
    },
  ];
