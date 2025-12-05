import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {OverviewSalesReport} from "../../domain/models/overview-sales-report.interface";
import {SalesReportFilters} from "../../domain/models/sales-report.interface";
import {SalesReportRepository} from "../../domain/repositories/sales-report.repository";
import {SalesReportAdapter} from "../../infrastructure/adapters/sales-report.adapter";

@Injectable({
  providedIn: 'root'
})
export class GetOverviewSalesReportUseCase {

  private readonly salesReportRepo: SalesReportRepository = inject(SalesReportAdapter);

  execute(filters: SalesReportFilters): Observable<OverviewSalesReport> {
    return this.salesReportRepo.getOverviewSalesReport(filters);
  }
}
