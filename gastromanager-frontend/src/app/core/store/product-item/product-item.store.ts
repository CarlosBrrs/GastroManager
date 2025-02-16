import {patchState, signalStore, withComputed, withHooks, withMethods, withState} from "@ngrx/signals";
import {computed, inject} from "@angular/core";
import {concatMap, Observable, tap} from "rxjs";
import {StoreEventService} from "../../services/store-event/store-event.service";
import {ProductItem} from "./product-item.model";
import {ProductItemService} from "../../services/product-item/product-item.service";
import {ProductItemRequestDto} from "../../model/interfaces/ProductItemRequestDto";
import {BaseStore} from "../base-store";

type ProductFilter = 'all' | 'cat1' | 'cat2';

type ProductItemState = {
  productItems: ProductItem[]
  category: ProductFilter,
  categories: string[],
  loading: boolean;
  error: string | null;
}

const initialState: ProductItemState = {
  productItems: [],
  category: 'all',
  categories: [],
  loading: false,
  error: null,
}

export const ProductItemStore = signalStore(
  {providedIn: "root"},
  withState(initialState),

  withMethods((store, productItemService = inject(ProductItemService), eventService = inject(StoreEventService)) => {
    const baseStore = new BaseStore(store, eventService);
    return {
      addProductItem: (productItem: ProductItemRequestDto): Observable<ProductItem[]> => {
        return baseStore.performOperation<ProductItem[]>(
          productItemService.addProductItem(productItem).pipe(
            tap(uuid => console.log("UUID del nuevo produto:", uuid)),
            concatMap(() => productItemService.getAllProductItems()),
          ),
          (productItems) => {
            console.log("Nuevos productos recuperados:", productItems);
            patchState(store, {productItems: productItems})
          },
          "producto agregado con éxito"
        );
      },
      updateProductItem: (uuid: string, productItem: ProductItemRequestDto): Observable<ProductItem[]> => {
        return baseStore.performOperation<ProductItem[]>(
          productItemService.updateProductItem(uuid, productItem).pipe(
            tap(updatedProductItem => console.log("Product Item editado:", updatedProductItem)),
            concatMap(() => productItemService.getAllProductItems()),
          ),
          (productItems) => {
            console.log("Lista de Product Item actualizada:", productItems);
            patchState(store, {productItems: productItems});
          },
          "product Item editado con éxito"
        );
      },
      loadProductItems: (): Observable<ProductItem[]> => {
        return baseStore.performOperation<ProductItem[]>(
          productItemService.getAllProductItems(),
          (response) => {
            const uniqueCategories = Array.from(new Set(response.map(item => item.category)));
            patchState(store, {productItems: response, categories: uniqueCategories});
          },
          "Productitems cargados con éxito"
        );
      },
      getCategories: (): string[] => store.categories()
    }
  }),
  withComputed((store) => ({
    productsByCategory: computed(() => (category: string) =>
      store.productItems().filter(item => item.category === category)
    ),
  })),
  withHooks({
    onInit(store) {
      store.loadProductItems();
    }
  })
);

