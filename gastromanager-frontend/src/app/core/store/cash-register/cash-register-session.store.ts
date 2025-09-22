import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {catchError, concatMap, delay, finalize, map, of, pipe, switchMap, tap, throwError} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {
  CashRegisterSession,
  CloseSessionData,
  OpenSessionData
} from "../../../features/management/domain/models/cash-register-session.interface";
import {
  OpenCashRegisterSessionUseCase
} from "../../../features/management/application/usecases/open-cash-register-session.use-case";
import {
  CloseCashRegisterSessionUseCase
} from "../../../features/management/application/usecases/close-cash-register-session.use-case";
import {
  GetSessionSummaryUseCase
} from "../../../features/management/application/usecases/get-session-summary.use-case";
import {CashRegisterStore} from "./cash-register.store";

type CashRegisterSessionState = {
  sessions: CashRegisterSession[];
  currentSession: CashRegisterSession | null;
  loading: boolean;
  error: string | null;
}

const initialState: CashRegisterSessionState = {
  sessions: [],
  currentSession: null,
  loading: false,
  error: null
}

export const CashRegisterSessionStore = signalStore(
    {providedIn: "root"},
    withState(initialState),
    withMethods((store,
                 openSessionUseCase = inject(OpenCashRegisterSessionUseCase),
                 closeSessionUseCase = inject(CloseCashRegisterSessionUseCase),
                 getSessionSummaryUseCase = inject(GetSessionSummaryUseCase),
                 cashRegisterStore = inject(CashRegisterStore)
    ) => ({

      openSession: (openSessionData: OpenSessionData) => {
        patchState(store, {loading: true, error: null});
        return openSessionUseCase.execute(openSessionData).pipe(
          tap(createdSession => {
            console.log('Session created with data:', createdSession);
            patchState(store, {
              currentSession: createdSession,
              sessions: [...store.sessions(), createdSession]
            });
          }),
          catchError((error: HttpErrorResponse) => {
            const message = error.message || 'Error desconocido';
            patchState(store, {error: message});
            return throwError(() => error);
          }),
          tap((createdSession) => {
            console.log("🔄 [CashRegisterSessionStore] Refreshing cash registers after session opened");
            // Llamar al rxMethod correctamente - solo dispara la acción, no retorna Observable
            cashRegisterStore.getAllCashRegisters();
            console.log("🔄 [CashRegisterSessionStore] Cash registers refresh triggered");
          }),
          finalize(() => {
            patchState(store, {loading: false});
          })
        )
      },
      closeSession: rxMethod<CloseSessionData>(
        pipe(
          tap(() => {
            patchState(store, {loading: true, error: null});
          }),
          switchMap((closeData) => {
            console.log("🔒 [CashRegisterSessionStore] Closing session via use case:", closeData);

            return closeSessionUseCase.execute(closeData).pipe(
              tapResponse({
                next: (closedSession) => {
                  console.log("✅ [CashRegisterSessionStore] Session closed successfully:", closedSession);

                  // Actualizar las sesiones en el store
                  const updatedSessions = store.sessions().map(session =>
                    session.uuid === closedSession.uuid ? closedSession : session
                  );

                  patchState(store, {
                    currentSession: null,
                    sessions: updatedSessions
                  });

                  // Refrescar cajas registradoras después de cerrar sesión
                  console.log("🔄 [CashRegisterSessionStore] Refreshing cash registers after session closed");
                  cashRegisterStore.getAllCashRegisters();
                },
                error: (error: HttpErrorResponse) => {
                  console.error("❌ [CashRegisterSessionStore] Error closing session:", error);
                  const message = error.message || 'Error al cerrar sesión';
                  patchState(store, {error: message});
                }
              }),
              finalize(() => patchState(store, {loading: false}))
            );
          })
        )
      ),
      getSessionsByCashRegister: rxMethod<string>(
        pipe(
          tap(() => {
            patchState(store, {loading: true, error: null});
          }),
          switchMap((cashRegisterUuid) => {
            // TODO: Implement actual service call
            console.log("📋 [CashRegisterSessionStore] Getting sessions for cash register:", cashRegisterUuid);

            return of([]).pipe(
              tapResponse({
                next: (sessions) => {
                  console.log("✅ [CashRegisterSessionStore] Sessions loaded:", sessions);
                  patchState(store, {sessions});
                },
                error: (error: HttpErrorResponse) => {
                  console.error("❌ [CashRegisterSessionStore] Error loading sessions:", error);
                  const message = error.message || 'Error al cargar sesiones';
                  patchState(store, {error: message});
                }
              }),
              finalize(() => patchState(store, {loading: false}))
            );
          })
        )
      ),
      // Actualizado método para obtener resumen de sesión usando use case sin UUID
      getSessionSummary: () => {
        console.log("📊 [CashRegisterSessionStore] Getting current session summary");
        patchState(store, {loading: true, error: null});

        // Llamar al use case sin UUID - el backend identifica por usuario autenticado
        return getSessionSummaryUseCase.execute().pipe(
          tap(sessionSummary => {
            console.log("✅ [CashRegisterSessionStore] Session summary loaded from API:", sessionSummary);
          }),
          catchError((error: HttpErrorResponse) => {
            console.error("❌ [CashRegisterSessionStore] Error loading session summary:", error);
            const message = error.message || 'Error al obtener resumen de sesión';
            patchState(store, {error: message});
            return throwError(() => error);
          }),
          finalize(() => {
            patchState(store, {loading: false});
          })
        );
      },

      clearError: () => {
        patchState(store, {error: null});
      },
      clearCurrentSession: () => {
        patchState(store, {currentSession: null});
      }
    })),


    withHooks({
      onInit(store) {
        console.log("🏪 [CashRegisterSessionStore] Initialized");
      }
    })
)
;
