import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {CashRegisterSession, CloseSessionData, OpenSessionData, SessionSummary} from '../../domain/models/cash-register-session.interface';
import {CashRegisterSessionsRepository} from '../../domain/ports/cash-register-sessions.repository';
import {ApiGenericResponse} from '../../../../core/model/interfaces/ApiGenericResponse';
import {CashRegisterSessionResponseDto, OpenSessionRequestDto, SessionSummaryResponseDto, CloseSessionRequestDto} from './dtos/cash-register-session.dto';
import {mapToCashRegisterSession, mapToSessionSummary} from '../../application/mappers/cash-register-session.mapper';

@Injectable({
  providedIn: 'root'
})
export class CashRegisterSessionsAdapter implements CashRegisterSessionsRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = 'http://localhost:8080/api/v1';

  openSession(openData: OpenSessionData): Observable<CashRegisterSession> {
    console.log('🔗 [CashRegisterSessionsAdapter] Opening session:', openData);

    const requestDto: OpenSessionRequestDto = {
      cashRegisterUuid: openData.cashRegisterUuid,
      openingAmount: openData.initialAmount,
      notes: openData.notes
    };

    return this.http.post<ApiGenericResponse<CashRegisterSessionResponseDto>>(
      `${this.baseUrl}/cash-register-sessions/open`,
      requestDto
    ).pipe(
      map((response: ApiGenericResponse<CashRegisterSessionResponseDto>) => {
        console.log('✅ [CashRegisterSessionsAdapter] Session opened successfully:', response);
        return mapToCashRegisterSession(response.data);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [CashRegisterSessionsAdapter] Error opening session:', error);
        throw new Error(error.error?.message || 'Error al abrir sesión de caja registradora');
      })
    );
  }

  closeSession(closeData: CloseSessionData): Observable<CashRegisterSession> {
    console.log('🔗 [CashRegisterSessionsAdapter] Closing session:', closeData);

    const requestDto: CloseSessionRequestDto = {
      closingAmount: closeData.finalAmount,
      notes: closeData.notes
    };

    return this.http.put<ApiGenericResponse<CashRegisterSessionResponseDto>>(
      `${this.baseUrl}/cash-register-sessions/${closeData.sessionUuid}/close`,
      requestDto
    ).pipe(
      map((response: ApiGenericResponse<CashRegisterSessionResponseDto>) => {
        console.log('✅ [CashRegisterSessionsAdapter] Session closed successfully:', response);
        return mapToCashRegisterSession(response.data);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [CashRegisterSessionsAdapter] Error closing session:', error);
        throw new Error(error.error?.message || 'Error al cerrar sesión de caja registradora');
      })
    );
  }

  getSessionSummary(): Observable<SessionSummary> {
    console.log('🔗 [CashRegisterSessionsAdapter] Getting current session summary');

    return this.http.get<SessionSummaryResponseDto>(
      `${this.baseUrl}/cash-register-sessions/current-summary`
    ).pipe(
      map((response: SessionSummaryResponseDto) => {
        console.log('✅ [CashRegisterSessionsAdapter] Session summary retrieved successfully:', response);
        return mapToSessionSummary(response.data);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ [CashRegisterSessionsAdapter] Error getting session summary:', error);
        throw new Error(error.error?.message || 'Error al obtener resumen de sesión');
      })
    );
  }
}
