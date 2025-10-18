import {Ingredient} from "../../../ingredients/domain/models/ingredient.interface";

export interface SelectedIngredient extends Ingredient {
  quantity: number;
}

