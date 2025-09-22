import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {CashRegisterSessionsRepository} from "../../domain/ports/cash-register-sessions.repository";
import {CashRegisterSessionsAdapter} from "../../infrastructure/api/cash-register-sessions.adapter";
import {SessionSummary} from "../../domain/models/cash-register-session.interface";

@Injectable({
  providedIn: 'root'
})
export class GetSessionSummaryUseCase {

  private readonly cashRegisterSessionsRepo: CashRegisterSessionsRepository = inject(CashRegisterSessionsAdapter);

  execute(): Observable<SessionSummary> {
    return this.cashRegisterSessionsRepo.getSessionSummary();
  }
}
