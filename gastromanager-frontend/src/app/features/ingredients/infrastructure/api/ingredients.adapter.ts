import {inject, Injectable} from '@angular/core';
import {IngredientsRepository} from "../../domain/ports/ingredients.repository";
import {catchError, Observable} from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {IngredientResponseDto} from "../../domain/models/ingredient-response-dto.interface";
import {Page} from "../../../../core/model/interfaces/pagination/page.interface";
import {
  mapToEditIngredientRequestDto,
  mapToIngredientDetail,
  mapToIngredientRequestDto,
  mapToIngredientSummary
} from "../../application/mappers/ingredient.mapper";
import {Ingredient} from "../../domain/models/ingredient.interface";
import {IngredientRequestDto} from "../../domain/models/ingredient-request-dto.interface";
import {IngredientDetailResponseDto} from "../../domain/models/ingredient-detail-response-dto.interface";
import {environment} from "../../../../../environments/environment";
import {AdjustStockRequest} from "../../domain/models/adjust-stock-request.interface";


@Injectable({
  providedIn: 'root'
})
export class IngredientsAdapter implements IngredientsRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;

  getAllIngredients(params: { page: number, size: number }): Observable<Page<Ingredient>> {
    const requestParams = new HttpParams()
      .set('page', params.page)
      .set('size', params.size);
    return this.http.get<ApiGenericResponse<Page<IngredientResponseDto>>>(`${this.baseUrl}/ingredients`,
      {
        params: requestParams
      }
    ).pipe(
      map((response: ApiGenericResponse<Page<IngredientResponseDto>>) => {
        const mappedContent = response.data.content.map(dto => mapToIngredientSummary(dto));
        return {
          ...response.data,
          content: mappedContent
        };
      }),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  getAllIngredientsNoPagination(): Observable<Ingredient[]> {
    return this.http.get<ApiGenericResponse<IngredientResponseDto[]>>(`${this.baseUrl}/ingredients/all`
    ).pipe(
      map((response: ApiGenericResponse<IngredientResponseDto[]>) =>
        response.data.map(dto => mapToIngredientSummary(dto))
      ),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  createIngredient(ingredient: Ingredient): Observable<string> {
    const mappedIngredient: IngredientRequestDto = mapToIngredientRequestDto(ingredient);
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/ingredients`, mappedIngredient
    ).pipe(
      map((response: ApiGenericResponse<string>) => response.data),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  getIngredientById(uuid: string): Observable<Ingredient> {
    return this.http.get<ApiGenericResponse<IngredientDetailResponseDto>>(`${this.baseUrl}/ingredients/${uuid}`,
    ).pipe(
      map((response: ApiGenericResponse<IngredientDetailResponseDto>) => mapToIngredientDetail(response.data)),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  editIngredient(uuid: string, ingredient: Ingredient): Observable<Ingredient> {
    const mappedIngredient: IngredientRequestDto = mapToEditIngredientRequestDto(ingredient);
    return this.http.put<ApiGenericResponse<IngredientResponseDto>>(`${this.baseUrl}/ingredients/${uuid}`, mappedIngredient
    ).pipe(
      map((response: ApiGenericResponse<IngredientResponseDto>) => response.data),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  adjustIngredientStock(ingredientUuid: string, request: AdjustStockRequest): Observable<string> {
    return this.http.patch<ApiGenericResponse<string>>(
      `${this.baseUrl}/ingredients/${ingredientUuid}/adjust-ingredient-stock`,
      request
    ).pipe(
      map((response: ApiGenericResponse<string>) => response.data),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

}
