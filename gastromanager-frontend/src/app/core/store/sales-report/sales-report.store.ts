import {patchState, signalStore, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {finalize, forkJoin, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {SalesReportFilters} from "../../../features/reports/domain/models/sales-report.interface";
import {OverviewSalesReport} from "../../../features/reports/domain/models/overview-sales-report.interface";
import {ProductSales} from "../../../features/reports/domain/models/product-sales.interface";
import {
  GetOverviewSalesReportUseCase
} from "../../../features/reports/application/usecases/get-overview-sales-report.use-case";
import {GetProductSalesUseCase} from "../../../features/reports/application/usecases/get-product-sales.use-case";

type SalesReportState = {
  data: OverviewSalesReport | null;
  productSales: ProductSales[] | null;
  loading: boolean;
  error: string | null;
  lastFilters: SalesReportFilters | null;
}

const initialState: SalesReportState = {
  data: null,
  productSales: null,
  loading: false,
  error: null,
  lastFilters: null
};

export const SalesReportStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store,
    getOverviewSalesReport = inject(GetOverviewSalesReportUseCase),
    getProductSales = inject(GetProductSalesUseCase)
  ) => ({

    getSalesReport: rxMethod<SalesReportFilters>(
      pipe(
        tap((filters) => {
          console.log('🔥 [SalesReportStore] Loading overview sales report with filters:', filters);
          patchState(store, {loading: true, error: null, lastFilters: filters});
        }),
        switchMap((filters) =>
          getOverviewSalesReport.execute(filters).pipe(
            tapResponse({
              next: (salesReport: OverviewSalesReport) => {
                console.log('✅ [SalesReportStore] Overview sales report loaded:', salesReport);
                patchState(store, {
                  data: salesReport,
                  error: null
                });
              },
              error: (error: HttpErrorResponse) => {
                console.error('❌ [SalesReportStore] Error loading overview sales report:', error);
                const message = error.message || 'Error desconocido';
                patchState(store, {
                  error: message
                });
              }
            }),
            finalize(() => {
              patchState(store, {loading: false});
            })
          )
        )
      )
    ),


    // Método combinado que carga overview y productos al mismo tiempo
    loadAllReportData: rxMethod<{filters: SalesReportFilters}>(
      pipe(
        tap(({filters}) => {
          console.log('🔥 [SalesReportStore] Loading all report data with filters:', filters);
          patchState(store, {loading: true, error: null, lastFilters: filters});
        }),
        switchMap(({filters}) => {
          // Cargar overview y productos al mismo tiempo usando los use cases
          return forkJoin({
            overview: getOverviewSalesReport.execute(filters),
            products: getProductSales.execute(filters)
          }).pipe(
            tapResponse({
              next: ({overview, products}) => {
                console.log('✅ [SalesReportStore] All report data loaded:', {overview, products});
                patchState(store, {
                  data: overview,
                  productSales: products,
                  error: null
                });
              },
              error: (error: HttpErrorResponse) => {
                console.error('❌ [SalesReportStore] Error loading report data:', error);
                const message = error.message || 'Error desconocido';
                patchState(store, {
                  error: message
                });
              }
            }),
            finalize(() => {
              patchState(store, {loading: false});
            })
          );
        })
      )
    ),

    clearSalesReport: () => {
      patchState(store, {
        data: null,
        productSales: null,
        error: null,
        lastFilters: null
      });
    }
  }))
);
