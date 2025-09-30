import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {DecimalPipe} from '@angular/common';
import {FiltersService} from '../../../infrastructure/services/filters.service';
import {SalesReportStore} from '../../../../../core/store/sales-report/sales-report.store';

@Component({
  selector: 'gm-sales-reports-page',
  standalone: true,
  imports: [
    DecimalPipe
  ],
  templateUrl: './sales-reports-page.component.html',
  styleUrl: './sales-reports-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SalesReportsPageComponent {
  private salesReportStore = inject(SalesReportStore);

  // Hacer público filtersService para acceder desde template
  readonly filtersService = inject(FiltersService);

  // Usar datos directamente del store
  salesData = computed(() => this.salesReportStore.data());
  loading = computed(() => this.salesReportStore.loading());
  activeFiltersCount = computed(() => this.filtersService.getActiveFiltersCount());

  // Computed para verificar si hay cambios pendientes
  hasUnappliedChanges = computed(() => {
    const currentFilters = this.filtersService.salesFilters();
    const lastAppliedFilters = this.salesReportStore.lastFilters();
    return JSON.stringify(currentFilters) !== JSON.stringify(lastAppliedFilters);
  });

  // Métodos que obtienen datos del store
  getTotalSales(): number {
    const data = this.salesData();
    return data ? data.totalSales : 0;
  }

  getTotalOrders(): number {
    const data = this.salesData();
    return data ? data.totalOrders : 0;
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
      console.log('Exportando métricas...', {
        totalSales: data.totalSales,
        totalOrders: data.totalOrders,
        summary: data.summary
      });
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
