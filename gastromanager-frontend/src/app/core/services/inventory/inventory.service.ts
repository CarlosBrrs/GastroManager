import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {Observable, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {IngredientResponseDto} from "../../model/interfaces/IngredientResponseDto";
import {IngredientDetailResponseDto} from "../../model/interfaces/IngredientDetailResponseDto";
import {IngredientRequestDto} from "../../model/interfaces/IngredientRequestDto";
import {IngredientItem} from "../../store/inventory/ingredient.model";

@Injectable({
  providedIn: 'root'
})
export class InventoryService extends BaseHttpService {

  getAllIngredientsTest(): Observable<ApiGenericResponse<IngredientItem[]>> {
    return this.http.get<ApiGenericResponse<IngredientItem[]>>(`${this.apiUrl}/ingredients`,
      {headers: {'Accept': 'application/json'}});
  }


  getAllIngredients(): Observable<ApiGenericResponse<IngredientResponseDto[]>> {
    return this.http.get<ApiGenericResponse<IngredientResponseDto[]>>(`${this.apiUrl}/ingredients`,
      {headers: {'Accept': 'application/json'}}).pipe(
      tap(response => {
        if (response.flag) {
          console.log('Ingredientes obtenidos:', response.data);
        }
      })
    );
  }

  addIngredient(ingredient: IngredientRequestDto): Observable<ApiGenericResponse<string>> {
    return this.http.post<ApiGenericResponse<string>>(`${this.apiUrl}/ingredients`, ingredient, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    }).pipe(
      tap(response => {
        if (response.flag) {
          console.log('Ingrediente añadido con UUID:', response.data);
        }
      })
    )

  }

  updateIngredient(uuid: string, ingredient: Partial<IngredientRequestDto>): Observable<ApiGenericResponse<IngredientDetailResponseDto>> {
    return this.http.put<ApiGenericResponse<IngredientDetailResponseDto>>(`${this.apiUrl}/ingredients/${uuid}`, ingredient, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    }).pipe(tap(response => {
      if (response.flag) {
        console.log('Ingrediente actualizado:', response.data);
      }
    }))
  }

  deleteIngredient(ingredientUuid: string): void {
    alert("not implemented yet");
  }
}
