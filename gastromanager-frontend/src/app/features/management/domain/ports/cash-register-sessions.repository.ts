import { Observable } from 'rxjs';
import { CashRegisterSession, OpenSessionData, CloseSessionData, SessionSummary } from '../models/cash-register-session.interface';

export interface CashRegisterSessionsRepository {
  openSession(openData: OpenSessionData): Observable<CashRegisterSession>;
  closeSession(closeData: CloseSessionData): Observable<CashRegisterSession>;
  getSessionSummary(): Observable<SessionSummary>; // Tipado con SessionSummary y sin parámetro UUID
}
