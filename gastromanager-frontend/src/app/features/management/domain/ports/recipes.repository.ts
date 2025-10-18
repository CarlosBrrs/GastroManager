import {Observable} from "rxjs";
import {Recipe} from "../models/recipe.interface";

export interface RecipesRepository {
  createRecipe(recipe: Partial<Recipe>): Observable<string>;
}

