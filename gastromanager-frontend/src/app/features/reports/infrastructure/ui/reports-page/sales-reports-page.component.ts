import {ChangeDetectionStrategy, Component, computed, inject, ViewEncapsulation} from '@angular/core';
import {CommonModule, DecimalPipe} from '@angular/common';
import {FiltersService} from '../../../infrastructure/services/filters.service';
import {SalesReportStore} from '../../../../../core/store/sales-report/sales-report.store';
import {TableModule} from 'primeng/table';

@Component({
  selector: 'gm-sales-reports-page',
  standalone: true,
  imports: [
    DecimalPipe,
    CommonModule,
    TableModule
  ],
  templateUrl: './sales-reports-page.component.html',
  styleUrl: './sales-reports-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class SalesReportsPageComponent {
  // Hacer público filtersService y salesReportStore para acceder desde template
  readonly filtersService = inject(FiltersService);
  activeFiltersCount = computed(() => this.filtersService.getActiveFiltersCount());
  readonly salesReportStore = inject(SalesReportStore);
  // Computed para datos del store
  salesData = computed(() => this.salesReportStore.data());
  loading = computed(() => this.salesReportStore.loading());
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
      // Cargar overview y productos al mismo tiempo (sin paginación, se maneja en el frontend)
      this.salesReportStore.loadAllReportData({filters});
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
      this.salesReportStore.loadAllReportData({filters: lastFilters});
    }
  }
}
