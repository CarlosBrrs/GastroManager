import {ProductIngredient} from "./product-ingredient.interface";
import {ProductRecipe} from "./product-recipe.interface";

export interface Product {
  uuid: string;
  name: string;
  description: string;
  salePrice: number;
  purchasePrice?: number;
  category: string;
  createdBy: string;
  createdDate: Date;
  updatedBy: string;
  updatedDate: Date;
  // Para modo avanzado
  recipes?: ProductRecipe[];
  ingredients?: ProductIngredient[];
}
