import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {CashRegisterSession, OpenSessionData} from '../../domain/models/cash-register-session.interface';
import {CashRegisterSessionsRepository} from '../../domain/ports/cash-register-sessions.repository';
import {CashRegisterSessionsAdapter} from '../../infrastructure/api/cash-register-sessions.adapter';

@Injectable({
  providedIn: 'root'
})
export class OpenCashRegisterSessionUseCase {
  private readonly cashRegisterSessionsRepo: CashRegisterSessionsRepository = inject(CashRegisterSessionsAdapter);

  execute(openData: OpenSessionData): Observable<CashRegisterSession> {
    return this.cashRegisterSessionsRepo.openSession(openData);
  }
}
