import {signalStore, withComputed, withHooks, withState} from "@ngrx/signals";
import {IngredientItem} from "./ingredient.model";
import {computed, effect, inject} from "@angular/core";
import {InventoryService} from "../../services/inventory/inventory.service";

type IngredientFilter = 'all' | 'cat1' | 'cat2';

type IngredientState = {
    ingredients: IngredientItem[]
    categoryFilter: IngredientFilter
}

const initialState: IngredientState = {
    ingredients: [],
    categoryFilter: 'all'
}

export const IngredientStore = signalStore(
        {providedIn: "root"},
        withState(initialState),
        // withMethods(),
        withComputed(({ingredients, categoryFilter}) => ({
            filteredIngredients: computed(() => {
                switch (categoryFilter()) {
                    case 'cat1':
                        return ingredients().filter(ingredient => {
                            return ingredient.category === 'cat1';
                        });
                    default:
                        return ingredients();
                }
            })
        })),
// can be used to implement data loading from localstorage or api with onInit and effect
        withHooks({
            onInit(store, inventoryService = inject(InventoryService)) {
                inventoryService.getAllIngredientsTest().subscribe((response) => {
                    console.log("from store... ", response);
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
    )
;

