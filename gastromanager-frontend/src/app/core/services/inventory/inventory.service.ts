import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {Observable} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {IngredientRequestDto} from "../../model/interfaces/IngredientRequestDto";
import {IngredientItem} from "../../store/inventory/ingredient.model";
import {AdjustStockRequestDto} from "../../../pages/inventory/inventory-table/inventory-table.component";

@Injectable({
  providedIn: 'root'
})
export class InventoryService extends BaseHttpService {

  getAllIngredientsTest(): Observable<ApiGenericResponse<IngredientItem[]>> {
    return this.http.get<ApiGenericResponse<IngredientItem[]>>(`${this.apiUrl}/ingredients`,
      {headers: {'Accept': 'application/json'}});
  }


  getAllIngredients(): Observable<IngredientItem[]> {
    return this.handleRequest<IngredientItem[]>("GET", 'ingredients');
  }

  addIngredient(ingredient: IngredientRequestDto): Observable<string> {
    return this.handleRequest<string>("POST", 'ingredients', ingredient);
  }

  updateIngredient(uuid: string, ingredient: Partial<IngredientRequestDto>): Observable<IngredientItem> {
    return this.handleRequest<IngredientItem>("PUT", `ingredients/${uuid}`, ingredient);
  }

  deleteIngredient(ingredientUuid: string): void {
    alert("not implemented yet");
  }

  adjustIngredientStock(uuid: string, stockAdjustment: AdjustStockRequestDto): Observable<string> {
    return this.handleRequest<string>("PATCH", `ingredients/${uuid}/adjust-ingredient-stock`, stockAdjustment);
  }
}
