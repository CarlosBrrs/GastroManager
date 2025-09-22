import {inject, Inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {CashRegisterSessionsRepository} from "../../domain/ports/cash-register-sessions.repository";
import {CashRegisterSessionsAdapter} from "../../infrastructure/api/cash-register-sessions.adapter";
import {CashRegisterSession, CloseSessionData} from "../../domain/models/cash-register-session.interface";

@Injectable({
  providedIn: 'root'
})
export class CloseCashRegisterSessionUseCase {

  private readonly cashRegisterSessionsRepo: CashRegisterSessionsRepository = inject(CashRegisterSessionsAdapter);

  execute(closeData: CloseSessionData): Observable<CashRegisterSession> {
    return this.cashRegisterSessionsRepo.closeSession(closeData);
  }
}
