import {inject, Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {IngredientsRepository} from "../../domain/ports/ingredients.repository";
import { IngredientsAdapter} from "../../infrastructure/api/ingredients.adapter";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Ingredient} from "../../domain/models/ingredient.interface";
import {IngredientRequestDto} from "../../domain/models/ingredient-request-dto.interface";

@Injectable({
  providedIn: 'root'
})
export class CreateIngredientUseCase {

  private readonly ingredientsRepo: IngredientsRepository = inject(IngredientsAdapter);

  execute(ingredient:Ingredient): Observable<string> {
    return this.ingredientsRepo.createIngredient(ingredient);
  }
}
