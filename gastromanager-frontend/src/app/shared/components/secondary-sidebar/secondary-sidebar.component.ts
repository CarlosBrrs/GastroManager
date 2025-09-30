import {Component, computed, inject} from '@angular/core';
import {RouterLink} from "@angular/router";
import {LayoutStore} from "../../../layouts/authenticated-layout/store/authenticated-layout.store";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {FiltersService} from "../../../features/reports/infrastructure/services/filters.service";

@Component({
  selector: 'gm-secondary-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    CommonModule,
    FormsModule
  ],
  templateUrl: './secondary-sidebar.component.html',
  styleUrl: './secondary-sidebar.component.scss'
})
export class SecondarySidebarComponent {
  layoutStore = inject(LayoutStore);
  private readonly filtersService = inject(FiltersService);

  links = computed(() => this.layoutStore.secondarySidebarLinks());
  // Cambiar para usar filtros dinámicos en lugar de estáticos
  filters = computed(() => this.layoutStore.dynamicModuleFilters());
  filterValues = computed(() => this.filtersService.filters());

  // Helper method para acceder a los valores de filtros de forma type-safe
  getFilterValue(key: string): any {
    const currentValue = this.filterValues()[key];
    // Si no hay valor actual, usar el defaultValue del filtro
    if (currentValue === undefined || currentValue === '') {
      const filters = this.filters();
      const filter = filters.find(f => f.key === key);
      return filter?.defaultValue || '';
    }
    return currentValue;
  }

  getFilterArray(key: string): any[] {
    const value = this.getFilterValue(key);
    return Array.isArray(value) ? value : [];
  }

  // Métodos para manejar eventos de filtros de forma type-safe
  onInputChange(filterKey: string, event: Event): void {
    const value = (event.target as HTMLInputElement)?.value || '';
    this.onFilterChange(filterKey, value);
  }

  onSelectChange(filterKey: string, event: Event): void {
    const value = (event.target as HTMLSelectElement)?.value || '';
    this.onFilterChange(filterKey, value);
  }

  onMultiSelectChange(filterKey: string, option: string, event: Event): void {
    const isChecked = (event.target as HTMLInputElement)?.checked || false;
    let currentValues = this.getFilterArray(filterKey);

    if (isChecked) {
      // Agregar valor si no existe
      if (!currentValues.includes(option)) {
        currentValues = [...currentValues, option];
      }
    } else {
      // Remover valor si existe
      currentValues = currentValues.filter((value: string) => value !== option);
    }

    this.onFilterChange(filterKey, currentValues);
  }

  // Método para seleccionar/deseleccionar todas las opciones
  toggleSelectAll(filterKey: string, options: string[]): void {
    const currentValues = this.getFilterArray(filterKey);
    const allSelected = options.every(option => currentValues.includes(option));

    if (allSelected) {
      // Deseleccionar todas
      this.onFilterChange(filterKey, []);
    } else {
      // Seleccionar todas
      this.onFilterChange(filterKey, [...options]);
    }
  }

  // Verificar si todas las opciones están seleccionadas
  areAllSelected(filterKey: string, options: string[]): boolean {
    const currentValues = this.getFilterArray(filterKey);
    return options.every(option => currentValues.includes(option));
  }

  // Verificar si al menos una opción está seleccionada (para estado indeterminado)
  hasPartialSelection(filterKey: string, options: string[]): boolean {
    const currentValues = this.getFilterArray(filterKey);
    const selectedCount = options.filter(option => currentValues.includes(option)).length;
    return selectedCount > 0 && selectedCount < options.length;
  }

  // Estado para controlar qué dropdown está abierto
  openDropdowns: Record<string, boolean> = {};

  toggleDropdown(filterKey: string): void {
    this.openDropdowns[filterKey] = !this.openDropdowns[filterKey];
  }

  isDropdownOpen(filterKey: string): boolean {
    return this.openDropdowns[filterKey] || false;
  }

  isOptionChecked(filterKey: string, option: string): boolean {
    const currentValues = this.getFilterArray(filterKey);
    return currentValues.includes(option);
  }

  onFilterChange(filterKey: string, value: any): void {
    console.log(`Filtro ${filterKey} cambió a:`, value);
    // Solo actualizar el estado de filtros, sin aplicar
    this.filtersService.updateFilter(filterKey, value);
  }

  onClearFilters(): void {
    console.log('Filtros limpiados');
    // Limpiar filtros a través del servicio centralizado
    this.filtersService.clearAllFilters();
  }
}
