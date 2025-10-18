import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {RecipesRepository} from "../../domain/ports/recipes.repository";
import {RecipesAdapter} from "../../infrastructure/api/recipes.adapter";
import {Recipe} from "../../domain/models/recipe.interface";

@Injectable({
  providedIn: 'root'
})
export class CreateRecipeUseCase {

  private readonly recipesRepo: RecipesRepository = inject(RecipesAdapter);

  execute(recipe: Partial<Recipe>): Observable<string> {
    return this.recipesRepo.createRecipe(recipe);
  }
}
