import {inject, Injectable} from '@angular/core';
import {catchError, Observable} from 'rxjs';
import {CashRegistersRepository} from '../../domain/ports/cash-registers.repository';
import {CashRegister, CashRegisterCreateData, CashRegisterOpenData, CashRegisterCloseData} from '../../domain/models/cash-register.interface';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {mapToCashRegister} from "../../application/mappers/cash-register.mapper";

// DTOs para la respuesta del backend
export interface CashRegisterResponseDto {
  uuid: string;
  name: string;
  location: string;
  status: string; // "OPEN" o "CLOSED"
  currentSession: CashRegisterCurrentSessionResponseDto | null;
}

export interface CashRegisterCurrentSessionResponseDto {
  uuid: string;
  openedAt: string;
  openedBy: string;
  openingAmount: number;
}

@Injectable({
  providedIn: 'root'
})
export class CashRegistersAdapter implements CashRegistersRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = 'http://localhost:8080/api/v1';

  getAllCashRegisters(): Observable<CashRegister[]> {
    console.log('🔄 [CashRegistersAdapter] Calling getAllCashRegisters');

    return this.http.get<ApiGenericResponse<CashRegisterResponseDto[]>>(`${this.baseUrl}/cash-registers`)
      .pipe(
        map((response: ApiGenericResponse<CashRegisterResponseDto[]>) => {
          console.log('✅ [CashRegistersAdapter] Respuesta exitosa de getAllCashRegisters:', response);
          const mappedContent = response.data.map(dto => {
            const mapped = mapToCashRegister(dto);
            console.log('✅ [CashRegistersAdapter] Caja registradora mapeada:', mapped);
            return mapped;
          });
          return mappedContent;
        }),
        catchError((error: HttpErrorResponse) => {
          console.error('❌ [CashRegistersAdapter] Error en getAllCashRegisters:', error);
          throw new Error(error.error?.message || 'Error al obtener cajas registradoras');
        })
      );
  }

/*  getCashRegisterByUuid(uuid: string): Observable<CashRegister> {
    console.log('🔄 [CashRegistersAdapter] Calling getCashRegisterByUuid with UUID:', uuid);
    return this.http.get<ApiGenericResponse<CashRegisterResponseDto>>(`${this.baseUrl}/cash-registers/${uuid}`)
      .pipe(
        map((response: ApiGenericResponse<CashRegisterResponseDto>) => {
          return mapToCashRegister(response.data);
        }),
        catchError((error: HttpErrorResponse) => {
          throw new Error(error.error?.message || 'Error al obtener caja registradora');
        })
      );
  }

  createCashRegister(cashRegister: CashRegisterCreateData): Observable<string> {
    console.log('🔄 [CashRegistersAdapter] Calling createCashRegister:', cashRegister);
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/cash-registers`, cashRegister)
      .pipe(
        map((response: ApiGenericResponse<string>) => response.data),
        catchError((error: HttpErrorResponse) => {
          throw new Error(error.error?.message || 'Error al crear caja registradora');
        })
      );
  }

  openCashRegister(openData: CashRegisterOpenData): Observable<string> {
    console.log('🔄 [CashRegistersAdapter] Calling openCashRegister:', openData);
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/cash-registers/${openData.cashRegisterUuid}/open`, openData)
      .pipe(
        map((response: ApiGenericResponse<string>) => response.data),
        catchError((error: HttpErrorResponse) => {
          throw new Error(error.error?.message || 'Error al abrir caja registradora');
        })
      );
  }

  closeCashRegister(closeData: CashRegisterCloseData): Observable<string> {
    console.log('🔄 [CashRegistersAdapter] Calling closeCashRegister:', closeData);
    return this.http.post<ApiGenericResponse<string>>(`${this.baseUrl}/cash-registers/sessions/${closeData.sessionUuid}/close`, closeData)
      .pipe(
        map((response: ApiGenericResponse<string>) => response.data),
        catchError((error: HttpErrorResponse) => {
          throw new Error(error.error?.message || 'Error al cerrar caja registradora');
        })
      );
  }

  updateCashRegister(uuid: string, cashRegister: Partial<CashRegister>): Observable<CashRegister> {
    console.log('🔄 [CashRegistersAdapter] Calling updateCashRegister:', uuid, cashRegister);
    return this.http.put<ApiGenericResponse<CashRegisterResponseDto>>(`${this.baseUrl}/cash-registers/${uuid}`, cashRegister)
      .pipe(
        map((response: ApiGenericResponse<CashRegisterResponseDto>) => {
          return mapToCashRegister(response.data);
        }),
        catchError((error: HttpErrorResponse) => {
          throw new Error(error.error?.message || 'Error al actualizar caja registradora');
        })
      );
  }

  deleteCashRegister(uuid: string): Observable<void> {
    console.log('🔄 [CashRegistersAdapter] Calling deleteCashRegister with UUID:', uuid);
    return this.http.delete<void>(`${this.baseUrl}/cash-registers/${uuid}`)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          throw new Error(error.error?.message || 'Error al eliminar caja registradora');
        })
      );
  }*/
}
