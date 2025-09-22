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
export class EditIngredientUseCase {

  private readonly ingredientsRepo: IngredientsRepository = inject(IngredientsAdapter);

  execute(uuid: string, ingredient:Ingredient): Observable<Ingredient> {
    return this.ingredientsRepo.editIngredient(uuid, ingredient);
  }
}
