import { Observable } from "rxjs";
import { SalesReport, SalesReportFilters } from "../models/sales-report.interface";

export interface SalesReportRepository {
  getSalesReport(filters: SalesReportFilters): Observable<SalesReport>;
}
