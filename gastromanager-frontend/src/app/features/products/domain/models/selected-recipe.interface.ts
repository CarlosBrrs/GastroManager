import {Recipe} from "../../../management/domain/models/recipe.interface";

export interface SelectedRecipe extends Recipe {
  multiplier: number;
}

