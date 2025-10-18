export interface RecipeIngredient {
  ingredientUuid: string;
  ingredientName?: string; // Opcional porque el backend puede obtenerlo del UUID
  quantity: number;
}

export interface Recipe {
  uuid: string;
  name: string;
  description: string;
  cost: number;
  ingredients: RecipeIngredient[];
  baseRecipe: Recipe | null;
  isEnabled: boolean;
  createdBy: string;
  updatedBy: string;
  createdDate: string;
  updatedDate: string;
}
