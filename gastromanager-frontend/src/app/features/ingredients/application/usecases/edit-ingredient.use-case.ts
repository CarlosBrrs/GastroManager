import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {IngredientsRepository} from "../../domain/ports/ingredients.repository";
import {IngredientsAdapter} from "../../infrastructure/api/ingredients.adapter";
import {Ingredient} from "../../domain/models/ingredient.interface";

@Injectable({
  providedIn: 'root'
})
export class EditIngredientUseCase {

  private readonly ingredientsRepo: IngredientsRepository = inject(IngredientsAdapter);

  execute(uuid: string, ingredient: Ingredient): Observable<Ingredient> {
    return this.ingredientsRepo.editIngredient(uuid, ingredient);
  }
}
