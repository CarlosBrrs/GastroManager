import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {SalesReport, SalesReportFilters} from "../../domain/models/sales-report.interface";
import {SalesReportRepository} from "../../domain/repositories/sales-report.repository";
import {SalesReportAdapter} from "../../infrastructure/adapters/sales-report.adapter";

@Injectable({
  providedIn: 'root'
})
export class GetSalesReportUseCase {

  private readonly salesReportRepo: SalesReportRepository = inject(SalesReportAdapter);

  execute(filters: SalesReportFilters): Observable<SalesReport> {
    return this.salesReportRepo.getSalesReport(filters);
  }
}
