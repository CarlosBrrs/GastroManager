import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {Observable, tap} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {IngredientResponseDto} from "../../model/interfaces/IngredientResponseDto";

@Injectable({
  providedIn: 'root'
})
export class InventoryService extends BaseHttpService {

  getAllIngredients(): Observable<ApiGenericResponse<IngredientResponseDto[]>> {
    return this.http.get<ApiGenericResponse<IngredientResponseDto[]>>(`${this.apiUrl}/ingredients`,
      {headers: {'Accept': 'application/json'}}).pipe(
      tap(response => {
        if (response.flag) {
        }
      })
    );
  }

  addIngredient(ingredient: any): Observable<ApiGenericResponse<string>> {
    return this.http.post<ApiGenericResponse<string>>(`${this.apiUrl}/ingredients`, ingredient, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    }).pipe(
      tap(response => {
        if (response.flag) {
        }
      })
    )

  }

  updateIngredient(uuid: string, ingredientWithoutUuid: any): Observable<ApiGenericResponse<IngredientResponseDto>> {
    return this.http.put<ApiGenericResponse<IngredientResponseDto>>(`${this.apiUrl}/ingredients/${uuid}`, ingredientWithoutUuid, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    }).pipe(tap(response => {
      if (response.flag) {
        console.log(response.data)
      }
    }))
  }

  deleteIngredient(ingredientUuid: string): void {
    alert("not implemented yet");
  }
}
