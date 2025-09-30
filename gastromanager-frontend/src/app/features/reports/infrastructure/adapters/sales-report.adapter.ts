import { inject, Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { map } from "rxjs/operators";
import { SalesReportRepository } from "../../domain/repositories/sales-report.repository";
import { SalesReport, SalesReportFilters } from "../../domain/models/sales-report.interface";
import { SalesReportResponseDto } from "../../domain/models/sales-report-response-dto.interface";
import { ApiGenericResponse } from "../../../../core/model/interfaces/ApiGenericResponse";
import { mapToSalesReport, mapToSalesReportFiltersDto } from "../../application/mappers/sales-report.mapper";
import { environment } from "../../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SalesReportAdapter implements SalesReportRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;

  getSalesReport(filters: SalesReportFilters): Observable<SalesReport> {
    const filtersDto = mapToSalesReportFiltersDto(filters);

    let requestParams = new HttpParams()
      .set('dateFrom', filtersDto.dateFrom)
      .set('dateTo', filtersDto.dateTo);

    if (filtersDto.cashRegisterUuids && filtersDto.cashRegisterUuids.length > 0) {
      filtersDto.cashRegisterUuids.forEach(uuid => {
        requestParams = requestParams.append('cashRegisterUuids', uuid);
      });
    }

    if (filtersDto.sessionState) {
      requestParams = requestParams.set('sessionState', filtersDto.sessionState);
    }

    if (filtersDto.paymentMethods && filtersDto.paymentMethods.length > 0) {
      filtersDto.paymentMethods.forEach(method => {
        requestParams = requestParams.append('paymentMethods', method);
      });
    }

    if (filtersDto.assignedUserUuids && filtersDto.assignedUserUuids.length > 0) {
      filtersDto.assignedUserUuids.forEach(uuid => {
        requestParams = requestParams.append('assignedUserUuids', uuid);
      });
    }

    return this.http.get<ApiGenericResponse<SalesReportResponseDto>>(`${this.baseUrl}/reports/sales`, {
      params: requestParams
    }).pipe(
      map((response: ApiGenericResponse<SalesReportResponseDto>) => {
        console.log("🚀 [SalesReportAdapter] Fetched sales report:", response);
        return mapToSalesReport(response.data);
      }),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error?.message || error.message || 'Error al obtener el reporte de ventas');
      })
    );
  }
}
