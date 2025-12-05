import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AdjustStockRequest} from '../../domain/models/adjust-stock-request.interface';
import {IngredientsAdapter} from '../../infrastructure/api/ingredients.adapter';

@Injectable({
  providedIn: 'root'
})
export class AdjustIngredientStockUseCase {
  private readonly ingredientsRepo: IngredientsAdapter = inject(IngredientsAdapter);

  execute(ingredientUuid: string, request: AdjustStockRequest): Observable<string> {
    return this.ingredientsRepo.adjustIngredientStock(ingredientUuid, request);
  }
}

