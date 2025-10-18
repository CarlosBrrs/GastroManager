import {patchState, signalStore, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {catchError, finalize, Observable, tap, throwError} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {CreateRecipeUseCase} from "../../../features/management/application/usecases/create-recipe.use-case";
import {Recipe} from "../../../features/management/domain/models/recipe.interface";

type RecipeState = {
  loading: boolean;
  error: string | null;
}

const initialState: RecipeState = {
  loading: false,
  error: null
}

export const RecipeStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store, createRecipeUseCase = inject(CreateRecipeUseCase)) => ({
    createRecipe: (recipe: Partial<Recipe>): Observable<string> => {
      patchState(store, {loading: true, error: null});
      return createRecipeUseCase.execute(recipe).pipe(
        tap(createdRecipeUuid => {
          console.log('Recipe created with uuid:', createdRecipeUuid);
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        finalize(() => {
          patchState(store, {loading: false});
        })
      );
    }
  }))
);
