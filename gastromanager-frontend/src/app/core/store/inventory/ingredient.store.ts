import {patchState, signalStore, withComputed, withHooks, withMethods, withState} from "@ngrx/signals";
import {IngredientItem} from "./ingredient.model";
import {computed, inject} from "@angular/core";
import {InventoryService} from "../../services/inventory/inventory.service";
import {concatMap, Observable, tap} from "rxjs";
import {IngredientRequestDto} from "../../model/interfaces/IngredientRequestDto";
import {StoreEventService} from "../../services/store-event/store-event.service";
import {BaseStore} from "../base-store";
import {AdjustStockRequestDto} from "../../../pages/inventory/inventory-table/inventory-table.component";

type IngredientFilter = 'all' | 'cat1' | 'cat2';

type IngredientState = {
  ingredients: IngredientItem[];
  category: IngredientFilter;
  loading: boolean;
  error: string | null;
}

const initialState: IngredientState = {
  ingredients: [],
  category: 'all',
  loading: false,
  error: null,
}

export const IngredientStore = signalStore(
  {providedIn: "root"},
  withState(initialState),

  withMethods((store, inventoryService = inject(InventoryService), eventService = inject(StoreEventService)) => {
    const baseStore = new BaseStore(store, eventService);
    return {
      addIngredient: (ingredient: IngredientRequestDto): Observable<IngredientItem[]> => {
        return baseStore.performOperation<IngredientItem[]>(
          inventoryService.addIngredient(ingredient).pipe(
            tap(uuid => console.log("UUID del nuevo ingrediente:", uuid)),
            concatMap(() => inventoryService.getAllIngredients()),
          ),
          (ingredientItems) => {
            console.log("Nuevos ingredients-page recuperados:", ingredientItems.toString());
            patchState(store, {ingredients: ingredientItems})
          },
          "Ingrediente agregado con éxito"
        );
      },
      editIngredient: (uuid: string, ingredient: IngredientRequestDto): Observable<IngredientItem[]> => {
        return baseStore.performOperation<IngredientItem[]>(
          inventoryService.updateIngredient(uuid, ingredient).pipe(
            tap(updatedIngredient => console.log("Ingrediente editado:", updatedIngredient)),
            concatMap(() => inventoryService.getAllIngredients()),
          ),
          (ingredientItems) => {
            console.log("Lista de ingredientes actualizada:", ingredientItems);
            patchState(store, {ingredients: ingredientItems});
          },
          "Ingrediente editado con éxito"
        );
      },
      adjustStock: (uuid: string, stockAdjustment: AdjustStockRequestDto): Observable<IngredientItem[]> => {
        return baseStore.performOperation<IngredientItem[]>(
          inventoryService.adjustIngredientStock(uuid, stockAdjustment).pipe(
            tap(uuid => console.log("UUID del ingrediente actualizado:", uuid)),
            concatMap(() => inventoryService.getAllIngredients()),
          ),
          (ingredientItems) => {
            console.log("Nuevos ingredients-page recuperados:", ingredientItems.toString());
            patchState(store, {ingredients: ingredientItems})
          },
          "Stock del ingrediente actualizado con éxito"
        );
      },
      loadIngredients: (): Observable<IngredientItem[]> => {
        return baseStore.performOperation<IngredientItem[]>(
          inventoryService.getAllIngredients(),
          (response) => patchState(store, {ingredients: response}),
          "Ingredientes cargados con éxito"
        );
      }
    }
  }),
  withComputed(({ingredients, category}) => ({
    filteredIngredients: computed(() => {
      switch (category()) {
        case 'cat1':
          return ingredients().filter(ingredient => ingredient.category === 'cat1');
        default:
          return ingredients();
      }
    })
  })),

  withHooks({
    onInit(store) {
      store.loadIngredients();
    }
  })
);

