import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ProductSales} from "../../domain/models/product-sales.interface";
import {SalesReportFilters} from "../../domain/models/sales-report.interface";
import {SalesReportRepository} from "../../domain/repositories/sales-report.repository";
import {SalesReportAdapter} from "../../infrastructure/adapters/sales-report.adapter";

@Injectable({
  providedIn: 'root'
})
export class GetProductSalesUseCase {

  private readonly salesReportRepo: SalesReportRepository = inject(SalesReportAdapter);

  execute(filters: SalesReportFilters): Observable<ProductSales[]> {
    return this.salesReportRepo.getProductSales(filters);
  }
}

