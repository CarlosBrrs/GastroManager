import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {catchError, concatMap, finalize, map, Observable, of, pipe, switchMap, tap, throwError} from "rxjs";
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
import {
  AdjustIngredientStockUseCase
} from "../../../features/ingredients/application/usecases/adjust-ingredient-stock.use-case";
import {AdjustStockRequest} from "../../../features/ingredients/domain/models/adjust-stock-request.interface";

export type ColumnProperties = {
  field: string;
  header: string;
  sortable?: boolean;
  transform?: (value: any, rowData?: any) => string; // Función inline con acceso a rowData
  pipe?: string; // Nombre del pipe a aplicar (nueva opción más elegante)
  pipeArgs?: any; // Argumentos para el pipe (ej: formato de fecha)
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
    {
      field: 'name',
      header: 'Nombre',
      prefix: '📦 '
    },
    {
      field: 'pricePerUnit',
      header: 'Precio por Unidad',
      pipe: 'currency',
      prefix: '💰 ',
      suffix: ' COP'
    },
    {
      field: 'unit',
      header: 'Unidad',
      pipe: 'unit'
    },
    {
      field: 'availableStock',
      header: 'Stock Disponible',
      pipe: 'stockStatus'
    },
    {
      field: 'minimumStockQuantity',
      header: 'Stock Mínimo',
      prefix: '⚠️ '
    },
    {
      field: 'supplier',
      header: 'Proveedor',
      prefix: '🏭 '
    },
    {
      field: 'updatedDate',
      header: 'Última Actualización',
      pipe: 'localDateTime',
      pipeArgs: 'short',
      prefix: '🕒 '
    }
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
               getAllIngredientsNoPagination = inject(GetAllIngredientsNoPaginationUseCase),
               adjustIngredientStock = inject(AdjustIngredientStockUseCase)) => ({
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
    clearError: () => patchState(store, {error: null}),
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
    adjustIngredientStock: (ingredientUuid: string, request: AdjustStockRequest): Observable<string> => {
      patchState(store, {loading: true, error: null});

      return adjustIngredientStock.execute(ingredientUuid, request).pipe(
        tap(responseUuid => {
          console.log('✅ Stock ajustado exitosamente para ingrediente:', responseUuid);
          // Limpiar el caché de todos los ingredientes
          patchState(store, {allIngredients: []});
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error al ajustar stock';
          patchState(store, {error: message, loading: false});
          return throwError(() => error);
        }),
        concatMap((responseUuid) => {
          console.log('Recargando lista de ingredientes después del ajuste...');

          // Recargar la página actual de ingredientes
          const params: paginationParams = {page: store.currentPage(), size: 7};
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
                const message = error.message || 'Error al recargar ingredientes';
                patchState(store, {error: message});
              }
            })
          ).pipe(
            map(() => responseUuid)
          );
        }),
        finalize(() => {
          patchState(store, {loading: false});
        })
      );
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
