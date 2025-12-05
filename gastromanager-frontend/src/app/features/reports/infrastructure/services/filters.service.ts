import {computed, effect, inject, Injectable, signal} from '@angular/core';
import {SalesReportFilters} from '../../domain/models/sales-report.interface';
import {CashRegisterStore} from '../../../../core/store/cash-register/cash-register.store';
import {LayoutStore} from '../../../../layouts/authenticated-layout/store/authenticated-layout.store';

@Injectable({
  providedIn: 'root'
})
export class FiltersService {
  private readonly cashRegisterStore = inject(CashRegisterStore);
  private readonly layoutStore = inject(LayoutStore);

  // Estado de filtros centralizado
  private readonly filterValues = signal<Record<string, any>>({});
  // Getters públicos (solo estado, no datos del store)
  readonly filters = this.filterValues.asReadonly();
  // Computed para convertir filtros a formato del backend
  readonly salesFilters = computed(() => {
    const filters = this.filterValues();

    return {
      dateFrom: filters['dateFrom'] ? this.convertLocalDateToUTCStart(filters['dateFrom']) : new Date(),
      dateTo: filters['dateTo'] ? this.convertLocalDateToUTCEnd(filters['dateTo']) : new Date(),
      cashRegisterUuids: this.mapCashRegistersToUuids(filters['cashRegister'] || []),
      sessionState: this.mapSessionsToState(filters['sessions'] || []),
      paymentMethods: filters['paymentMethods'] || [],
      assignedUserUuids: filters['assignedUserUuids'] || []
    } as SalesReportFilters;
  });

  constructor() {
    // Inicializar valores por defecto cuando cambien los filtros del módulo
    this.initializeDefaultValues();
  }

  // Actualizar filtros desde el secondary sidebar
  updateFilter(key: string, value: any): void {
    console.log(`🔄 [FiltersService] Updating filter ${key}:`, value);

    this.filterValues.update(current => ({
      ...current,
      [key]: value
    }));
  }

  // Limpiar todos los filtros
  clearAllFilters(): void {
    console.log('🧹 [FiltersService] Clearing all filters');
    this.filterValues.set({});
  }

  // Contar filtros activos
  getActiveFiltersCount(): number {
    const filters = this.filterValues();
    return Object.keys(filters).filter(key => {
      const value = filters[key];
      const b = Array.isArray(value) ? value.length > 0 : true;
      return value && (typeof value === 'string' ? value.trim() !== '' : b);
    }).length;
  }

  // Verificar si los filtros tienen datos válidos
  hasValidFilters(): boolean {
    const filters = this.salesFilters();
    return !!(filters.dateFrom && filters.dateTo);
  }

  private initializeDefaultValues(): void {
    // Effect para inicializar valores por defecto cuando cambien los filtros dinámicos
    effect(() => {
      const currentModule = this.layoutStore.currentModule();
      const filters = this.layoutStore.dynamicModuleFilters();

      if (currentModule && filters.length > 0) {
        console.log(`🔧 [FiltersService] Initializing default values for module: ${currentModule}`);

        // Inicializar valores por defecto solo para filtros que los tengan
        const defaultValues: Record<string, any> = {};

        filters.forEach(filter => {
          if (filter.defaultValue !== undefined) {
            // Solo setear el valor por defecto si no existe un valor actual
            const currentValue = this.filterValues()[filter.key];
            if (currentValue === undefined || currentValue === '') {
              defaultValues[filter.key] = filter.defaultValue;
              console.log(`📅 [FiltersService] Setting default value for ${filter.key}:`, filter.defaultValue);
            }
          }
        });

        // Actualizar valores solo si hay valores por defecto que setear
        if (Object.keys(defaultValues).length > 0) {
          this.filterValues.update(current => ({
            ...current,
            ...defaultValues
          }));
        }
      }
    }, {allowSignalWrites: true});
  }

  private mapCashRegistersToUuids(cashRegisterNames: string[]): string[] {
    const cashRegisters = this.cashRegisterStore.cashRegisters();

    // Mapear nombres seleccionados a UUIDs reales
    return cashRegisterNames
      .map(name => {
        const cashRegister = cashRegisters.find(cr => cr.name === name);
        return cashRegister ? cashRegister.uuid : null;
      })
      .filter(uuid => uuid !== null) as string[];
  }

  private mapSessionsToState(sessions: string[]): 'ALL' | 'OPEN' | 'CLOSED' {
    if (sessions.length === 0 || sessions.length === 2) {
      return 'ALL';
    }
    if (sessions.includes('Abiertas')) {
      return 'OPEN';
    }
    if (sessions.includes('Cerradas')) {
      return 'CLOSED';
    }
    return 'ALL';
  }

  /**
   * Convierte una fecha local (YYYY-MM-DD) al inicio del día en UTC
   * Ejemplo: '2025-09-29' → 2025-09-29T05:00:00.000Z (si estás en UTC-5)
   */
  private convertLocalDateToUTCStart(dateString: string): Date {
    // Crear fecha local a las 00:00:00 en la zona del usuario
    const localDate = new Date(dateString + 'T00:00:00');

    // Log para debugging
    console.log(`📅 [FiltersService] Converting dateFrom: ${dateString} (local) → ${localDate.toISOString()} (UTC)`);

    return localDate;
  }

  /**
   * Convierte una fecha local (YYYY-MM-DD) al final del día en UTC
   * Ejemplo: '2025-09-29' → 2025-09-30T04:59:59.999Z (si estás en UTC-5)
   */
  private convertLocalDateToUTCEnd(dateString: string): Date {
    // Crear fecha local a las 23:59:59.999 en la zona del usuario
    const localDate = new Date(dateString + 'T23:59:59.999');

    // Log para debugging
    console.log(`📅 [FiltersService] Converting dateTo: ${dateString} (local) → ${localDate.toISOString()} (UTC)`);

    return localDate;
  }
}
