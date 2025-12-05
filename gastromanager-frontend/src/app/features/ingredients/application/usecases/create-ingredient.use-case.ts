import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {IngredientsRepository} from "../../domain/ports/ingredients.repository";
import {IngredientsAdapter} from "../../infrastructure/api/ingredients.adapter";
import {Ingredient} from "../../domain/models/ingredient.interface";

@Injectable({
  providedIn: 'root'
})
export class CreateIngredientUseCase {

  private readonly ingredientsRepo: IngredientsRepository = inject(IngredientsAdapter);

  execute(ingredient: Ingredient): Observable<string> {
    return this.ingredientsRepo.createIngredient(ingredient);
  }
}
