import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {effect, inject} from "@angular/core";
import {tapResponse} from "@ngrx/operators";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {pipe, switchMap, tap} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {Restaurant} from "../../../features/restaurants/domain/models/restaurant.interface";
import {
  UserRestaurantResponseDto
} from "../../../features/restaurants/domain/models/user-restaurant-response-dto.interface";
import {
  GetUserRestaurantsUseCase
} from "../../../features/restaurants/application/usecases/get-user-restaurants.use-case";
import {
  GetRestaurantByUuidUseCase
} from "../../../features/restaurants/application/usecases/get-restaurant-by-uuid.use-case";
import {AuthStore} from "../auth/auth.store";

type RestaurantState = {
  userRestaurants: UserRestaurantResponseDto[];
  selectedRestaurantUuid: string;
  currentRestaurantDetails: Restaurant | null;
  restaurantDetailsCache: Record<string, Restaurant>; // Caché de detalles por UUID
  loading: boolean;
  error: string | null;
  userRestaurantsLoaded: boolean; // Para controlar si ya se cargaron
}

const initialState: RestaurantState = {
  userRestaurants: [],
  selectedRestaurantUuid: localStorage.getItem('selectedRestaurantUuid') || '',
  currentRestaurantDetails: null,
  restaurantDetailsCache: {}, // Inicializar caché vacío
  loading: false,
  error: null,
  userRestaurantsLoaded: false
}

export const RestaurantStore = signalStore(
  {providedIn: "root",},
  withState(initialState),
  withMethods((store,
               getUserRestaurants = inject(GetUserRestaurantsUseCase),
               getRestaurantDetails = inject(GetRestaurantByUuidUseCase)
  ) => ({

    // Carga SOLO los restaurantes del usuario (sin detalles)
    loadUserRestaurants: rxMethod<void>(
      pipe(
        tap(() => patchState(store, {loading: true, error: null})),
        switchMap(() =>
          getUserRestaurants.execute().pipe(
            tapResponse({
              next: (restaurants: UserRestaurantResponseDto[]) => {
                console.log("User restaurants loaded:", restaurants);
                const firstRestaurantUuid = restaurants[0]?.uuid || '';
                patchState(store, {
                  userRestaurants: restaurants,
                  selectedRestaurantUuid: firstRestaurantUuid,
                  userRestaurantsLoaded: true
                });
                // NO llamamos loadRestaurantDetails aquí - se hará en el effect
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error loading user restaurants';
                patchState(store, {error: message});
                console.error("Error loading user restaurants:", error);
              },
              finalize: () => patchState(store, {loading: false})
            })
          )
        )
      )
    ),

    // Carga SOLO los detalles de un restaurante específico
    loadRestaurantDetails: rxMethod<string>(
      pipe(
        tap(() => patchState(store, {loading: true, error: null})),
        switchMap((uuid: string) =>
          getRestaurantDetails.execute(uuid).pipe(
            tapResponse({
              next: (restaurant: Restaurant) => {
                console.log("Restaurant details loaded from API:", restaurant);
                patchState(store, {
                  currentRestaurantDetails: restaurant,
                  restaurantDetailsCache: {
                    ...store.restaurantDetailsCache(),
                    [uuid]: restaurant // Guardar en caché
                  }
                });
              },
              error: (error: HttpErrorResponse) => {
                const message = error.message || 'Error loading restaurant details';
                patchState(store, {error: message});
                console.error("Error loading restaurant details:", error);
              },
              finalize: () => patchState(store, {loading: false})
            })
          )
        )
      )
    ),

    // Cambia el restaurante seleccionado (con lógica de caché inteligente)
    selectRestaurant: (restaurantUuid: string) => {
      console.log("Selecting restaurant UUID:", restaurantUuid);
      localStorage.setItem('selectedRestaurantUuid', restaurantUuid);

      const cachedRestaurant = store.restaurantDetailsCache()[restaurantUuid];

      if (cachedRestaurant) {
        console.log("Loading restaurant details from cache:", restaurantUuid);
        // Si está en caché, cargar directamente
        patchState(store, {
          selectedRestaurantUuid: restaurantUuid,
          currentRestaurantDetails: cachedRestaurant
        });
      } else {
        console.log("Restaurant not in cache, will load from API:", restaurantUuid);
        // Si no está en caché, solo actualizar el UUID (el effect cargará desde API)
        patchState(store, {selectedRestaurantUuid: restaurantUuid});
      }
    }
  })),
  withHooks({
    onInit: (store, authStore = inject(AuthStore)) => {
      // Effect que se ejecuta SOLO cuando el usuario se autentica por primera vez
      effect(() => {
        const isAuthenticated = authStore.isAuthenticated();
        const userRestaurantsLoaded = store.userRestaurantsLoaded();

        // Solo cargar restaurantes si está autenticado Y no se han cargado antes
        if (isAuthenticated && !userRestaurantsLoaded) {
          console.log("User authenticated - loading restaurants for the first time");
          store.loadUserRestaurants();
        }
      }, {allowSignalWrites: true});

      // Effect que carga los detalles cuando cambia el restaurante seleccionado
      effect(() => {
        const selectedRestaurantUuid = store.selectedRestaurantUuid();
        const userRestaurantsLoaded = store.userRestaurantsLoaded();
        const cachedRestaurant = store.restaurantDetailsCache()[selectedRestaurantUuid];
        const currentRestaurant = store.currentRestaurantDetails();

        // Solo cargar desde API si:
        // 1. Hay un restaurante seleccionado
        // 2. Ya se cargaron los restaurantes del usuario
        // 3. NO está en caché
        // 4. NO es el mismo que ya está cargado actualmente
        if (selectedRestaurantUuid &&
          userRestaurantsLoaded &&
          !cachedRestaurant &&
          currentRestaurant?.uuid !== selectedRestaurantUuid) {
          console.log("Effect: Loading details from API for restaurant:", selectedRestaurantUuid);
          store.loadRestaurantDetails(selectedRestaurantUuid);
        } else if (selectedRestaurantUuid && cachedRestaurant && currentRestaurant?.uuid !== selectedRestaurantUuid) {
          console.log("Effect: Restaurant found in cache, loading from memory:", selectedRestaurantUuid);
          // Si está en caché pero no es el actual, cargar desde caché
          patchState(store, {
            currentRestaurantDetails: cachedRestaurant
          });
        }
      }, {allowSignalWrites: true});
    }
  })
);
