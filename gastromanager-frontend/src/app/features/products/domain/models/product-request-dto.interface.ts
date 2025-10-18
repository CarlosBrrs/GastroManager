import {ProductIngredient} from "./product-ingredient.interface";
import {ProductRecipe} from "./product-recipe.interface";

export interface ProductRequestDto {
  name: string;
  description: string;
  salePrice: number;
  purchasePrice?: number;
  category: string;
  // Para modo avanzado
  recipes?: ProductRecipe[];
  ingredients?: ProductIngredient[];
}
