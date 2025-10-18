import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {catchError, concatMap, finalize, Observable, of, pipe, switchMap, tap, throwError} from "rxjs";
import {GetIngredientsUseCase} from "../../../features/ingredients/application/usecases/get-ingredients.use-case";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {paginationParams} from "../../model/interfaces/pagination/pagination-params.interface";
import {CreateIngredientUseCase} from "../../../features/ingredients/application/usecases/create-ingredient.use-case";
import {Ingredient} from "../../../features/ingredients/domain/models/ingredient.interface";
import {
  GetIngredientByUuidUseCase
} from "../../../features/ingredients/application/usecases/get-ingredient-by-uuid.use-case";
import {EditIngredientUseCase} from "../../../features/ingredients/application/usecases/edit-ingredient.use-case";
import {Page} from "../../model/interfaces/pagination/page.interface";
import {
  GetAllIngredientsNoPaginationUseCase
} from "../../../features/ingredients/application/usecases/get-all-ingredients-no-pagination.use-case";

export type ColumnProperties = {
  field: string;
  header: string;
  sortable?: boolean;
  transform?: (value: any) => string; // Función inline (actual)
  pipe?: string; // Nombre del pipe a aplicar (nueva opción más elegante)
  prefix?: string; // Texto a agregar al inicio
  suffix?: string; // Texto a agregar al final
};
type InventoryState = {
  pages: Map<number, Ingredient[]>;
  currentPage: number;
  totalRecords: number;
  tableColumns: Array<ColumnProperties>;
  loading: boolean;
  error: string | null;
  ingredientToEdit: Ingredient | null;
  selectedIngredient: Ingredient | null;
  allIngredients: Ingredient[];
}

const initialState: InventoryState = {
  pages: new Map<number, Ingredient[]>(),
  currentPage: 0,
  totalRecords: 0,
  tableColumns: [
    {field: 'name', header: 'Nombre'},
    {field: 'pricePerUnit', header: 'Precio por Unidad'},
    {field: 'unit', header: 'Unidad'},
    {field: 'availableStock', header: 'Stock Disponible'},
    {field: 'minimumStockQuantity', header: 'Stock Mínimo'},
    {field: 'supplier', header: 'Proveedor'},
    // {field: 'createdBy', header: 'Creado Por'},
    // {field: 'createdDate', header: 'Fecha de Creación'},
    // {field: 'updatedBy', header: 'Actualizado Por'},
    {field: 'updatedDate', header: 'Fecha de Actualización'}
  ],
  selectedIngredient: null,
  loading: false,
  error: null,
  ingredientToEdit: null,
  allIngredients: []
}

export const InventoryStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store,
               getIngredients = inject(GetIngredientsUseCase),
               createIngredient = inject(CreateIngredientUseCase),
               editIngredient = inject(EditIngredientUseCase),
               getIngredientByUuid = inject(GetIngredientByUuidUseCase),
               getAllIngredientsNoPagination = inject(GetAllIngredientsNoPaginationUseCase)) => ({
    createIngredient: (ingredient: Ingredient): Observable<Page<Ingredient>> => {
      patchState(store, {loading: true, error: null});
      return createIngredient.execute(ingredient).pipe(
        tap(createdIngredientUuid => {
          console.log('Ingredient created with uuid:', createdIngredientUuid);
          // Limpiar el caché de todos los ingredientes
          patchState(store, {allIngredients: []});
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        concatMap((createdIngredientUuid) => {
          console.log('New ingredient with UUID added to the list, reloading...:', createdIngredientUuid);
          let params: paginationParams = {page: 0, size: 7};
          return getIngredients.execute(params).pipe(
            tapResponse({
              next: (response) => {
                const {number, content} = response;
                const updatedPages = new Map(store.pages());
                updatedPages.set(number, content);
                patchState(store, {
                  pages: updatedPages,
                  currentPage: params.page,
                  totalRecords: response.totalElements
                });
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
            })
          )
        }),
        finalize(() => {
          patchState(store, {loading: false});
        })
      )
    },
    getIngredients: rxMethod<paginationParams>(
      pipe(
        tap(() => {
          patchState(store, {loading: true, error: null});
        }),
        switchMap((params) => {
          const page = params.page;
          if (store.pages().has(page)) {
            patchState(store, {currentPage: page, loading: false, error: null});
            return of(store.pages().get(page));
          }
          return getIngredients.execute(params).pipe(
            tapResponse({
              next: (response) => {
                const {number, content} = response;
                const updatedPages = new Map(store.pages());
                updatedPages.set(number, content);
                patchState(store, {pages: updatedPages, currentPage: page, totalRecords: response.totalElements});
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
              finalize: () => {
                patchState(store, {loading: false})
              }
            })
          )
        })
      )),
    getIngredientById: rxMethod<{ uuid: string, forEdit?: boolean }>(
      pipe(
        tap(() => {
          patchState(store, {loading: true, error: null});
        }),
        switchMap(({uuid, forEdit}) => {
          return getIngredientByUuid.execute(uuid).pipe(
            tapResponse({
              next: (response) => {
                if (forEdit) {
                  patchState(store, {ingredientToEdit: response});
                } else {
                  patchState(store, {selectedIngredient: response});
                }
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
              finalize: () => {
                patchState(store, {loading: false})
              }
            })
          )
        })
      )
    ),
    clearIngredientToEdit: () => patchState(store, {ingredientToEdit: null}),
    clearSelectedIngredient: () => patchState(store, {selectedIngredient: null}),
    getAllIngredientsNoPagination: rxMethod<void>(
      pipe(
        tap(() => {
          patchState(store, {loading: true, error: null});
        }),
        switchMap(() => {
          // Si ya tenemos todos los ingredientes cargados, los retornamos
          if (store.allIngredients().length > 0) {
            patchState(store, {loading: false});
            return of(store.allIngredients());
          }
          // Si no, hacemos la petición al servidor
          return getAllIngredientsNoPagination.execute().pipe(
            tapResponse({
              next: (ingredients) => {
                patchState(store, {allIngredients: ingredients});
                console.log('✅ Todos los ingredientes cargados:', ingredients.length);
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
                console.error('❌ Error al cargar ingredientes:', message);
              },
              finalize: () => {
                patchState(store, {loading: false})
              }
            })
          )
        })
      )
    ),
    editIngredient: (ingredient: Ingredient): Observable<Page<Ingredient>> => {
      patchState(store, {loading: true, error: null});
      return editIngredient.execute(ingredient.uuid, ingredient).pipe(
        tap(updatedIngredient => {
          console.log('Ingredient updated with info:', updatedIngredient);
          // Limpiar el caché de todos los ingredientes
          patchState(store, {allIngredients: []});
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        concatMap((updatedIngredient) => {
          console.log('Ingredient with UUID updated, reloading...:', updatedIngredient.uuid);
          let params: paginationParams = {page: 0, size: 7};
          return getIngredients.execute(params).pipe(
            tapResponse({
              next: (response) => {
                const {number, content} = response;
                const updatedPages = new Map(store.pages());
                updatedPages.set(number, content);
                patchState(store, {
                  pages: updatedPages,
                  currentPage: params.page,
                  totalRecords: response.totalElements
                });
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
              finalize: () => {
                patchState(store, {loading: false})
              }
            })
          )
        }),
        finalize(() => {
          patchState(store, {loading: false});
        })
      )
    },
  })),
  withHooks(
    {
      onInit: (store) => {
        store.getIngredients({page: 0, size: 7});
      }
    }
  )
);
