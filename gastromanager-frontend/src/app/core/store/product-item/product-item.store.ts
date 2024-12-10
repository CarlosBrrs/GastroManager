import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {effect, inject} from "@angular/core";
import {catchError, concatMap, of} from "rxjs";
import {StoreEventService} from "../../services/store-event/store-event.service";
import {ProductItem} from "./product-item.model";
import {ProductItemService} from "../../services/product-item/product-item.service";
import {ProductItemRequestDto} from "../../model/interfaces/ProductItemRequestDto";

type ProductFilter = 'all' | 'cat1' | 'cat2';

type ProductItemState = {
  productItems: ProductItem[]
  category: ProductFilter
}

const initialState: ProductItemState = {
  productItems: [],
  category: 'all'
}

export const ProductItemStore = signalStore(
  {providedIn: "root"},
  withState(initialState),

  withMethods((store, productItemService = inject(ProductItemService), eventService = inject(StoreEventService)) => ({
    addProductItem: (productItem: ProductItemRequestDto) => {
      productItemService.addProductItem(productItem).pipe(
        concatMap((response) => {
          eventService.emitSuccess(`Product item added successfully`, response.message);
          return productItemService.getAllProductItemsTest()
        }),
        catchError((error) => {
          console.log(error)
          eventService.emitError("Error adding product item", error.error.message);
          return of({
            data: store.productItems()
          });
        })
      ).subscribe({
        next: (response) => {
          patchState(store, {productItems: response?.data || []}); // Actualiza la lista de ingredientes
          console.log("Lista de ingredientes obtenida:", response);
        },
        error: (error) => {
          eventService.emitError("Error adding ingredient", error.error.message);
          console.error("Error al agregar o refrescar los ingredientes:", error);
        },
        complete: () => {
          console.log("completed add ingredient in store")
        }
      });
    },
    editProductItem: (uuid: string, productItem: Partial<ProductItemRequestDto>) => {
      productItemService.updateProductItem(uuid, productItem)
        .pipe(
          concatMap((response) => {
            eventService.emitSuccess(`Product item updated successfully`, response.message);
            return productItemService.getAllProductItemsTest()
          }),
          catchError((error) => {
            console.log(error)
            eventService.emitError("Error updating product items", error.error.message);
            return of({
              data: store.productItems()
            });
          })
        ).subscribe({
        next: (response) => {
          patchState(store, {productItems: response?.data || []}); // Actualiza la lista de ingredientes
          console.log("Lista de product items actualizada:", response);
        },
        error: error => {
          eventService.emitError("Error updating product items", error.error.message);
          console.error("Error al actualizar los product items:", error);
        },
        complete: () => {
          console.log("completed update product items in store")
        }
      })
    },
    /*    deleteIngredient: (ingredientUuid: string) => {
        },*/
    /*getIngredients: () => {
      inventoryService.getAllIngredientsTest()
        .pipe(
          finalize(() => {
            // to set the state of the store, finalize will get triggered always after the rest of the subscribe props
            //  this.loading.set(false)
            console.log("completed");
          }))
        .subscribe({
          next: (response) => {
            // this.ingredients.set(response.data);
            // this.loading.set(false)
            patchState(store, {ingredients: response?.data || []}); // Actualiza la lista de ingredientes
            console.log("Lista de ingredientes actualizada:", response);
            eventService.emitSuccess(`Ingredients obtenidos agregado`);
          },
          error: error => {
            /!*messageService.add({
              severity: 'error',
              summary: 'Error loading ingredients',
              detail: error.error.message
            });*!/
            console.log("error loading ingredients", error)
            // this.error.set('Error loading ingredients');
          },
          complete: () => {
            console.log("completed successfully")
          }
        });
    },*/
    /*    changeCategory: (category: IngredientFilter) => {
          patchState(store, {category});
        }*/
  })),
  /*withComputed(({ingredients, category}) => ({
    filteredIngredients: computed(() => {
      switch (category()) {
        case 'cat1':
          return ingredients().filter(ingredient => {
            return ingredient.category === 'cat1';
          });
        default:
          return ingredients();
      }
    })
  })),*/
  // can be used to implement data loading from localstorage or api with onInit and effect
  withHooks({
    onInit(store, productItemService = inject(ProductItemService), eventService = inject(StoreEventService)) {
      console.log("loading store")
      productItemService.getAllProductItemsTest()
        .pipe(catchError((error) => {
          console.log("Error fetching data", error);
          return of(undefined);
        }))
        .subscribe((response) => {
            patchState(store, {productItems: response?.data});
            console.log("ProductItems retrieved successfully ", response);
          }
        )
      effect(() => {
        console.log("loading hook oninit in store")
      });
    },
    onDestroy(store) {
      effect(() => {
        console.log("loading hook ondestroy in store")
      })
    },
  })
);

