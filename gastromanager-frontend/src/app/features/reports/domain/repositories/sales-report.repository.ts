import {Observable} from "rxjs";
import {SalesReport, SalesReportFilters} from "../models/sales-report.interface";
import {OverviewSalesReport} from "../models/overview-sales-report.interface";

export interface SalesReportRepository {
  getSalesReport(filters: SalesReportFilters): Observable<SalesReport>;

  getOverviewSalesReport(filters: SalesReportFilters): Observable<OverviewSalesReport>;
}
