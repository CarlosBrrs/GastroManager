import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StoreEventService {

  successSignal = signal<string | null>(null);
  successHeaderSignal = signal<string | undefined>(undefined);
  errorSignal = signal<string | null>(null);
  errorHeaderSignal = signal<string | undefined>(undefined);

  // Métodos para emitir eventos
  emitSuccess(header: string, message: string) {
    this.successHeaderSignal.set(header);
    this.successSignal.set(message);
  }

  emitError(header: string, message: string) {
    this.errorHeaderSignal.set(header);
    this.errorSignal.set(message);
  }
  constructor() { }
}
