import {patchState, signalStore, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {finalize, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {SalesReportFilters} from "../../../features/reports/domain/models/sales-report.interface";
import {OverviewSalesReport} from "../../../features/reports/domain/models/overview-sales-report.interface";
import {
  GetOverviewSalesReportUseCase
} from "../../../features/reports/application/usecases/get-overview-sales-report.use-case";

type SalesReportState = {
  data: OverviewSalesReport | null;
  loading: boolean;
  error: string | null;
  lastFilters: SalesReportFilters | null;
}

const initialState: SalesReportState = {
  data: null,
  loading: false,
  error: null,
  lastFilters: null
};

export const SalesReportStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store, getOverviewSalesReport = inject(GetOverviewSalesReportUseCase)) => ({

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

    clearSalesReport: () => {
      patchState(store, {
        data: null,
        error: null,
        lastFilters: null
      });
    }
  }))
);
