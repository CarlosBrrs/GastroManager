import {Observable} from "rxjs";
import {Ingredient} from "../models/ingredient.interface";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {AdjustStockRequest} from "../models/adjust-stock-request.interface";

export interface IngredientsRepository {

  getAllIngredients(params: { page: number, size: number }): Observable<Page<Ingredient>>;

  getAllIngredientsNoPagination(): Observable<Ingredient[]>;

  createIngredient(ingredient: Ingredient): Observable<string>;

  getIngredientById(uuid: string): Observable<Ingredient>;

  editIngredient(uuid: string, ingredient: Ingredient): Observable<Ingredient>;

  adjustIngredientStock(ingredientUuid: string, request: AdjustStockRequest): Observable<string>;
}
