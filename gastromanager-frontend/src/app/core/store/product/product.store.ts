import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {ColumnProperties} from "../inventory/inventory.store";
import {Product} from "../../../features/products/domain/models/product.interface";
import {ProductsByCategory} from "../../../features/products/domain/models/products-by-category.interface";
import {GetProductsUseCase} from "../../../features/products/application/usecases/get-products.use-case";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {paginationParams} from "../../model/interfaces/pagination/pagination-params.interface";
import {catchError, concatMap, finalize, Observable, of, pipe, switchMap, tap, throwError} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {CreateProductUseCase} from "../../../features/products/application/usecases/create-product.use-case";
import {Page} from "../../model/interfaces/pagination/page.interface";
import {
  GetProductsGroupedByCategoryUseCase
} from "../../../features/products/application/usecases/get-products-grouped-by-category.use-case";

type ProductState = {
  pages: Map<number, Product[]>;
  currentPage: number;
  totalRecords: number;
  tableColumns: Array<ColumnProperties>;
  loading: boolean;
  error: string | null;
  productToEdit: Product | null;
  selectedProduct: Product | null;
  productsByCategory: ProductsByCategory | null;
  loadingCategories: boolean;
}

const initialState: ProductState = {
  pages: new Map<number, Product[]>(),
  currentPage: 0,
  totalRecords: 0,
  tableColumns: [
    {field: 'name', header: 'Nombre'},
    {field: 'description', header: 'Descripción'},
    {field: 'salePrice', header: 'Precio de venta'},
    {field: 'purchasePrice', header: 'Precio de compra'},
    /*        {field: 'category', header: 'Categoría'},
            {field: 'createdBy', header: 'Creado Por'},
            {field: 'createdDate', header: 'Fecha de Creación'},
            {field: 'updatedBy', header: 'Actualizado Por'},
            {field: 'updatedDate', header: 'Fecha de Actualización'}*/
  ],
  selectedProduct: null,
  loading: false,
  error: null,
  productToEdit: null,
  productsByCategory: null,
  loadingCategories: false
}

interface groupedParams {
  groupBy?: string;
  includeEmpty?: boolean;
  isEnabled?: boolean;
}

export const ProductStore = signalStore(
  {providedIn: "root",},
  withState(initialState),
  withMethods((store,
               getProducts = inject(GetProductsUseCase),
               createProduct = inject(CreateProductUseCase),
               getProductsGroupedByCategoryUseCase = inject(GetProductsGroupedByCategoryUseCase),
    ) => ({
      getProducts: rxMethod<paginationParams>(
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
              return getProducts.execute(params).pipe(
                tapResponse({
                  next: (response) => {
                    const {number, content} = response;
                    const updatedPages = new Map(store.pages());
                    updatedPages.set(number, content);
                    patchState(store, {
                      pages: updatedPages,
                      currentPage: page,
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
            }
          )
        )),
      createProduct: (product: Product): Observable<Page<Product>> => {
        patchState(store, {loading: true, error: null});
        return createProduct.execute(product).pipe(
          tap(createdProductUuid => {
            console.log('Product created with uuid:', createdProductUuid);
          }),
          catchError((error: HttpErrorResponse) => {
            const message = error.message || 'Error desconocido';
            patchState(store, {error: message});
            return throwError(() => error);
          }),
          concatMap((createdProductUuid) => {
            console.log('New Product with UUID added to the list, reloading...:', createdProductUuid);
            patchState(store, {productsByCategory: null});
            let params: paginationParams = {page: 0, size: 7};
            return getProducts.execute(params).pipe(
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
      getProductsGroupedByCategory: rxMethod<groupedParams>(
        pipe(
          tap(() => {
            patchState(store, {loadingCategories: true, error: null});
          }),
          switchMap((params) => {
            if (!params && store.productsByCategory()) {
              patchState(store, {loadingCategories: false});
              return of(store.productsByCategory());
            }

            return getProductsGroupedByCategoryUseCase.execute(params).pipe(
              tapResponse({
                next: (response) => {
                  patchState(store, {
                    productsByCategory: response
                  });
                },
                error: (error: HttpErrorResponse) => {
                  const message = error.message || 'Error desconocido';
                  patchState(store, {error: message});
                },
                finalize: () => {
                  patchState(store, {loadingCategories: false});
                }
              })
            );
          })
        )
      ),
      clearProductsByCategory: () => patchState(store, {productsByCategory: null})


      /*createIngredient: (ingredient: Ingredient): Observable<Page<Ingredient>> => {
        patchState(store, {loading: true, error: null});
        return createIngredient.execute(ingredient).pipe(
          tap(createdIngredientUuid => {
            console.log('Ingredient created with uuid:', createdIngredientUuid);
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
      getIngredientById: rxMethod<{ uuid: string, forEdit?: boolean}>(
        pipe(
          tap(() => {
            patchState(store, {loading: true, error: null});
          }),
          switchMap(({ uuid, forEdit }) => {
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
      editIngredient: (ingredient: Ingredient): Observable<Page<Ingredient>> => {
        patchState(store, {loading: true, error: null});
        return editIngredient.execute(ingredient.uuid, ingredient).pipe(
          tap(updatedIngredient => {
            console.log('Ingredient updated with info:', updatedIngredient);
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
      },*/
    }),
  ),
  withHooks(
    {
      onInit: (store) => {
        store.getProducts({page: 0, size: 7});
      }
    }
  )
);
