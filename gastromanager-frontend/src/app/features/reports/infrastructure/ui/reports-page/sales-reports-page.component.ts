import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {DecimalPipe, CommonModule} from '@angular/common';
import {FiltersService} from '../../../infrastructure/services/filters.service';
import {SalesReportStore} from '../../../../../core/store/sales-report/sales-report.store';

@Component({
  selector: 'gm-sales-reports-page',
  standalone: true,
  imports: [
    DecimalPipe,
    CommonModule
  ],
  templateUrl: './sales-reports-page.component.html',
  styleUrl: './sales-reports-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SalesReportsPageComponent {
  private readonly salesReportStore = inject(SalesReportStore);

  // Hacer público filtersService para acceder desde template
  readonly filtersService = inject(FiltersService);

  // Computed para datos del store
  salesData = computed(() => this.salesReportStore.data());
  loading = computed(() => this.salesReportStore.loading());
  activeFiltersCount = computed(() => this.filtersService.getActiveFiltersCount());

  // Computed para verificar si hay cambios pendientes
  hasUnappliedChanges = computed(() => {
    const currentFilters = this.filtersService.salesFilters();
    const lastAppliedFilters = this.salesReportStore.lastFilters();
    return JSON.stringify(currentFilters) !== JSON.stringify(lastAppliedFilters);
  });

  // ===== MÉTODOS =====

  // Métodos que obtienen datos del salesData
  getTotalRevenue(): number {
    const data = this.salesData();
    return data?.summary?.totalRevenue || 0;
  }

  getTotalOrders(): number {
    const data = this.salesData();
    return data?.summary?.totalOrders || 0;
  }

  getCompletedOrders(): number {
    const data = this.salesData();
    return data?.summary?.completedOrders || 0;
  }

  getCancelledOrders(): number {
    const data = this.salesData();
    return data?.summary?.cancelledOrders || 0;
  }

  getCancellationRate(): number {
    const data = this.salesData();
    return data?.summary?.cancellationRate || 0;
  }

  getTotalOrderValue(): number {
    const data = this.salesData();
    return data?.summary?.totalOrderValue || 0;
  }

  getTotalTips(): number {
    const data = this.salesData();
    return data?.summary?.totalTips || 0;
  }

  getTotalPaidWithTips(): number {
    const data = this.salesData();
    return data?.summary?.totalPaidWithTips || 0;
  }

  getPendingAmount(): number {
    const data = this.salesData();
    return data?.summary?.pendingAmount || 0;
  }

  getCollectionRate(): number {
    const data = this.salesData();
    return data?.summary?.collectionRate || 0;
  }

  getAverageOrderValue(): number {
    const data = this.salesData();
    return data?.summary?.averageOrderValue || 0;
  }

  getTotalDays(): number {
    const data = this.salesData();
    return data?.filters?.totalDays || 0;
  }

  getActiveFiltersCount(): number {
    return this.activeFiltersCount();
  }


  // Método para aplicar filtros - ahora desde el componente
  applyFilters(): void {
    const filters = this.filtersService.salesFilters();

    if (this.filtersService.hasValidFilters()) {
      console.log('🔄 [SalesReportsPage] Applying filters:', filters);
      this.salesReportStore.getSalesReport(filters);
    } else {
      console.warn('⚠️ [SalesReportsPage] Cannot apply filters - missing required dates');
    }
  }

  // Métodos de acciones
  exportToExcel(): void {
    const data = this.salesData();
    if (data) {
      console.log('Exportando métricas...', data);
    }
    // TODO: Implementar exportación real
  }

  refreshData(): void {
    console.log('Actualizando datos...');
    // Llamar directamente al store para refrescar con los últimos filtros aplicados
    const lastFilters = this.salesReportStore.lastFilters();
    if (lastFilters) {
      this.salesReportStore.getSalesReport(lastFilters);
    }
  }
}
