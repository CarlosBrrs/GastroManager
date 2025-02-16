import {patchState, WritableStateSource} from "@ngrx/signals";
import {finalize, Observable, tap} from "rxjs";
import {StoreEventService} from "../services/store-event/store-event.service";
import {HttpErrorResponse} from "@angular/common/http";

export class BaseStore {
  constructor(private readonly store: WritableStateSource<any>, private readonly events: StoreEventService) {
  }

  performOperation<T>(operation: Observable<T>, successCallback: (data: T) => void, successMessage: string): Observable<T> {
    patchState(this.store, {loading: true, error: null});
    return operation.pipe(
      tap({
        next: (response) => {
          successCallback(response);
          patchState(this.store, {error: null});
          this.events.emitSuccess("ÉXITO", successMessage);
        },
        error: error => {
          const message = error instanceof HttpErrorResponse ? error.error.message : 'Error desconocido';
          patchState(this.store, {error: message});
          this.events.emitError("FALLIDO", message);
        }
      }),
      finalize(() => {
        patchState(this.store, {loading: false});
      })
    );
  }
}
