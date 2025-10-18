import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {DecimalPipe, CommonModule} from '@angular/common';
import {FiltersService} from '../../../infrastructure/services/filters.service';
import {SalesReportStore} from '../../../../../core/store/sales-report/sales-report.store';

// Interfaces para ventas por producto
interface ProductSales {
  uuid: string;
  productName: string;
  quantitySold: number;
  totalRevenue: number;
  averagePrice: number;
  orderCount: number;
  percentage: number;
}

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

  // ===== NUEVOS DATOS DUMMY PARA PRODUCTOS =====

  // Selector para mostrar top productos
  selectedTopCount = 10;
  topCountOptions = [
    { value: 10, label: 'Top 10' },
    { value: 20, label: 'Top 20' },
    { value: 50, label: 'Todos' }
  ];

  // Datos dummy de ventas por producto
  dummyProductSales: ProductSales[] = [
    { uuid: '1', productName: 'Hamburguesa Clásica', quantitySold: 45, totalRevenue: 675000, averagePrice: 15000, orderCount: 32, percentage: 18.5 },
    { uuid: '2', productName: 'Pizza Margherita', quantitySold: 38, totalRevenue: 570000, averagePrice: 15000, orderCount: 28, percentage: 15.6 },
    { uuid: '3', productName: 'Pollo a la Plancha', quantitySold: 35, totalRevenue: 525000, averagePrice: 15000, orderCount: 25, percentage: 14.4 },
    { uuid: '4', productName: 'Ensalada César', quantitySold: 32, totalRevenue: 384000, averagePrice: 12000, orderCount: 24, percentage: 10.5 },
    { uuid: '5', productName: 'Pasta Carbonara', quantitySold: 28, totalRevenue: 420000, averagePrice: 15000, orderCount: 21, percentage: 11.5 },
    { uuid: '6', productName: 'Tacos Mexicanos', quantitySold: 25, totalRevenue: 250000, averagePrice: 10000, orderCount: 18, percentage: 6.9 },
    { uuid: '7', productName: 'Salmón Grillado', quantitySold: 22, totalRevenue: 440000, averagePrice: 20000, orderCount: 17, percentage: 12.1 },
    { uuid: '8', productName: 'Cerveza Corona', quantitySold: 85, totalRevenue: 255000, averagePrice: 3000, orderCount: 45, percentage: 7.0 },
    { uuid: '9', productName: 'Mojito', quantitySold: 42, totalRevenue: 210000, averagePrice: 5000, orderCount: 28, percentage: 5.8 },
    { uuid: '10', productName: 'Cheesecake', quantitySold: 18, totalRevenue: 144000, averagePrice: 8000, orderCount: 15, percentage: 3.9 }
  ];

  // Computed para productos filtrados
  filteredProductSales = computed(() => {
    const topCount = this.selectedTopCount;
    return this.dummyProductSales
      .sort((a, b) => b.totalRevenue - a.totalRevenue)
      .slice(0, topCount === 50 ? this.dummyProductSales.length : topCount);
  });

  // Método para formatear montos sin el formato K
  formatCurrency(value: number): string {
    return '$' + value.toLocaleString('es-CO');
  }

  // Método para formatear porcentajes
  formatPercentage(value: number): string {
    return value.toFixed(1) + '%';
  }

  // ===== MÉTODOS EXISTENTES =====

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

  // ===== NUEVOS MÉTODOS PARA PRODUCTOS =====

  onTopCountChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedTopCount = parseInt(target.value);
  }

  onProductRowSelect(product: ProductSales): void {
    console.log('📦 Product selected:', product);
    // Aquí podrías abrir un modal con detalles del producto o navegar a otra vista
  }

  // ===== MÉTODOS EXISTENTES =====

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
