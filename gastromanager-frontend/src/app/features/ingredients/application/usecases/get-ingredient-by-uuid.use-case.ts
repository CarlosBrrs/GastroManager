import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {IngredientsRepository} from "../../domain/ports/ingredients.repository";
import { IngredientsAdapter} from "../../infrastructure/api/ingredients.adapter";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Ingredient} from "../../domain/models/ingredient.interface";

@Injectable({
  providedIn: 'root'
})
export class GetIngredientByUuidUseCase {

  private readonly ingredientsRepo: IngredientsRepository = inject(IngredientsAdapter);

  execute(uuid: string): Observable<Ingredient> {
    return this.ingredientsRepo.getIngredientById(uuid);
  }
}
