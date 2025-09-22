import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {IngredientsRepository} from "../../domain/ports/ingredients.repository";
import { IngredientsAdapter} from "../../infrastructure/api/ingredients.adapter";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {Ingredient} from "../../domain/models/ingredient.interface";

@Injectable({
  providedIn: 'root'
})
export class GetIngredientsUseCase {

  private readonly ingredientsRepo: IngredientsRepository = inject(IngredientsAdapter);

  execute(params: {page: number, size: number}): Observable<Page<Ingredient>> {
    return this.ingredientsRepo.getAllIngredients(params);
  }
}
