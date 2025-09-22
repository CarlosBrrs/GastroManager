import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {effect, inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {catchError, finalize, of, pipe, switchMap, tap, throwError} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {
  CashRegister,
  CashRegisterCreateData,
  CashRegisterOpenData,
  CashRegisterCloseData
} from "../../../features/management/domain/models/cash-register.interface";
import {GetAllCashRegistersUseCase} from "../../../features/management/application/usecases/get-all-cash-registers.use-case";
import {RestaurantStore} from "../restaurant/restaurant.store";

type CashRegisterState = {
  cashRegisters: CashRegister[];
  loading: boolean;
  error: string | null;
  selectedCashRegister: CashRegister | null;
}

const initialState: CashRegisterState = {
  cashRegisters: [],
  loading: false,
  error: null,
  selectedCashRegister: null
}

export const CashRegisterStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store, getAllCashRegistersUseCase = inject(GetAllCashRegistersUseCase)) => ({

    getAllCashRegisters: rxMethod<void>(
      pipe(
        tap(() => {
          patchState(store, {loading: true, error: null});
        }),
        switchMap(() => {
          return getAllCashRegistersUseCase.getAllCashRegisters().pipe(
            tapResponse({
              next: (response) => {
                console.log("✅ [CashRegisterStore] Cash registers loaded:", response);
                patchState(store, {cashRegisters: response});
              },
              error: (error: HttpErrorResponse) => {
                console.error("❌ [CashRegisterStore] Error loading cash registers:", error);
                const message = error.message || 'Error desconocido';
                patchState(store, {error: message});
              },
              finalize: () => {
                patchState(store, {loading: false});
              }
            })
          );
        })
      )
    ),
/*
    getCashRegisterByUuid: (uuid: string) => {
      patchState(store, {loading: true, error: null});
      return cashRegisterService.getCashRegisterByUuid(uuid).pipe(
        tap(cashRegister => {
          console.log('📦 Cash register retrieved by UUID:', cashRegister);
          patchState(store, {selectedCashRegister: cashRegister});
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
    },

    createCashRegister: (cashRegisterData: CashRegisterCreateData) => {
      patchState(store, {loading: true, error: null});
      return cashRegisterService.createCashRegister(cashRegisterData).pipe(
        tap(createdUuid => {
          console.log('✅ [CashRegisterStore] Cash register created with UUID:', createdUuid);
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        finalize(() => {
          // Recargar la lista después de crear
          store.getAllCashRegisters();
          patchState(store, {loading: false});
        })
      );
    },

    openCashRegister: (openData: CashRegisterOpenData) => {
      patchState(store, {loading: true, error: null});
      return cashRegisterService.openCashRegister(openData).pipe(
        tap(sessionUuid => {
          console.log('✅ [CashRegisterStore] Cash register opened with session UUID:', sessionUuid);
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        finalize(() => {
          // Recargar la lista después de abrir
          store.getAllCashRegisters();
          patchState(store, {loading: false});
        })
      );
    },

    closeCashRegister: (closeData: CashRegisterCloseData) => {
      patchState(store, {loading: true, error: null});
      return cashRegisterService.closeCashRegister(closeData).pipe(
        tap(result => {
          console.log('✅ [CashRegisterStore] Cash register closed:', result);
        }),
        catchError((error: HttpErrorResponse) => {
          const message = error.message || 'Error desconocido';
          patchState(store, {error: message});
          return throwError(() => error);
        }),
        finalize(() => {
          // Recargar la lista después de cerrar
          store.getAllCashRegisters();
          patchState(store, {loading: false});
        })
      );
    },

    clearSelectedCashRegister: () => {
      patchState(store, {selectedCashRegister: null});
    },

    clearError: () => {
      patchState(store, {error: null});
    }*/

  })),
  withHooks({
    onInit: (store) => {
      const restaurantStore = inject(RestaurantStore);

      // Effect que reacciona cuando cambia el restaurante seleccionado
      effect(() => {
        const restaurantUuid = restaurantStore.selectedRestaurantUuid();
        console.log('🔄 [OrderStore] Restaurant UUID changed:', restaurantUuid);

        // Solo cargar órdenes si tenemos un restaurante válido
        if (restaurantUuid && restaurantUuid.trim() !== '') {
          console.log('✅ [CashRegisterStore] Loading cash registers for restaurant:', restaurantUuid);
          store.getAllCashRegisters();
        } else {
          console.log('⚠️ [CashRegisterStore] No restaurant UUID available, skipping orders load');
        }
      }, {allowSignalWrites: true});
    }
  })
);
