import {inject, Injectable} from '@angular/core';
import {RecipesRepository} from "../../domain/ports/recipes.repository";
import {Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {Recipe} from "../../domain/models/recipe.interface";
import {environment} from "../../../../../environments/environment";
import {map} from "rxjs/operators";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";

@Injectable({
  providedIn: 'root'
})
export class RecipesAdapter implements RecipesRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;

  createRecipe(recipe: Partial<Recipe>): Observable<string> {
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/recipes`, recipe)
      .pipe(
        map((response: ApiGenericResponse<string>) => response.data)
      );
  }
}

