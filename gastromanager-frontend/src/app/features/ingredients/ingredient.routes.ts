import {Routes} from "@angular/router";

export const ingredientRoutes: Routes = [
  {
    path: 'ingredients',
    loadComponent: () => import('./infrastructure/ui/ingredients-page/ingredients-page.component')
      .then(m => m.IngredientsPageComponent),
  }, {
    path: 'ingredients/create',
    loadComponent: () => import('./infrastructure/ui/create-ingredient-page/create-update-ingredient-page.component')
      .then(m => m.CreateUpdateIngredientPageComponent)
  },
  {
    path: 'ingredients/:uuid/edit',
    loadComponent: () => import('./infrastructure/ui/create-ingredient-page/create-update-ingredient-page.component')
      .then(m => m.CreateUpdateIngredientPageComponent)
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'ingredients'
  },
];
