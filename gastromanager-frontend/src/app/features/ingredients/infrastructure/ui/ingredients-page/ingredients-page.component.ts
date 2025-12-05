import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {IngredientsTableComponent} from "../ingredients-table/ingredients-table.component";
import {ColumnProperties, InventoryStore} from "../../../../../core/store/inventory/inventory.store";
import {Router} from "@angular/router";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {
  DetailSection,
  EntityDetailSidebarComponent
} from "../../../../../shared/components/entity-detail-sidebar/entity-detail-sidebar.component";
import {Ingredient} from "../../../domain/models/ingredient.interface";
import {
  InventoryAdjustmentFormComponent,
  InventoryAdjustmentFormValue
} from "../../../../../shared/components/inventory-adjustment-form/inventory-adjustment-form.component";

@Component({
  selector: 'gm-ingredients-page',
  standalone: true,
  imports: [
    IngredientsTableComponent,
    EntityDetailSidebarComponent,
  ],
  templateUrl: './ingredients-page.component.html',
  styleUrl: './ingredients-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IngredientsPageComponent {

  selectedIngredient = signal<Ingredient | null>(null);
  sidebarVisible: boolean = false;
  private readonly ingredientsStore = inject(InventoryStore);
  ingredients = computed(() => {
    const page = this.ingredientsStore.currentPage();
    return this.ingredientsStore.pages().get(page) || [];
  });
  totalRecords = computed(() => this.ingredientsStore.totalRecords());
  loading = computed(() => this.ingredientsStore.loading());
  tableColumns: ColumnProperties[] = this.ingredientsStore.tableColumns();
  // Computed desde el store para el loading del formulario
  adjustmentLoading = computed(() => this.ingredientsStore.loading());
  // Computed para el error del formulario
  adjustmentError = computed(() => this.ingredientsStore.error());
  // Computed para las secciones del sidebar
  detailSections = computed<DetailSection[]>(() => {
    const ingredient = this.ingredientsStore.selectedIngredient();
    if (!ingredient) return [];

    return [
      {
        title: 'Información General',
        icon: '📋',
        fields: [
          {
            label: 'Nombre',
            value: ingredient.name,
            icon: '📦'
          },
          {
            label: 'Proveedor',
            value: ingredient.supplier,
            icon: '🏭'
          },
          {
            label: 'Unidad de Medida',
            value: ingredient.unit,
            icon: '📏',
            pipe: 'unit'
          }
        ]
      },
      {
        title: 'Información de Inventario',
        icon: '📊',
        fields: [
          {
            label: 'Precio por Unidad',
            value: ingredient.pricePerUnit,
            pipe: 'currency',
            icon: '💰',
            suffix: ' COP'
          },
          {
            label: 'Stock Disponible',
            value: ingredient.availableStock,
            pipe: 'stockStatus',
            pipeArgs: {minStock: ingredient.minimumStockQuantity, unit: ingredient.unit},
            icon: '📦',
            highlight: ingredient.availableStock < ingredient.minimumStockQuantity
          },
          {
            label: 'Stock Mínimo',
            value: ingredient.minimumStockQuantity,
            pipe: 'stockStatus',
            pipeArgs: {unit: ingredient.unit},
            icon: '⚠️'
          }
        ]
      },
      {
        title: 'Metadatos',
        icon: '🕒',
        fields: [
          {
            label: 'Creado por',
            value: ingredient.createdBy,
            icon: '👤'
          },
          {
            label: 'Fecha de Creación',
            value: ingredient.createdDate,
            pipe: 'localDateTime',
            pipeArgs: 'medium',
            icon: '📅'
          },
          {
            label: 'Actualizado por',
            value: ingredient.updatedBy,
            icon: '👤'
          },
          {
            label: 'Última Actualización',
            value: ingredient.updatedDate,
            pipe: 'localDateTime',
            pipeArgs: 'short',
            icon: '📅'
          }
        ]
      },
      {
        title: 'Ajuste de Inventario',
        icon: '📝',
        actions: [
          {
            component: InventoryAdjustmentFormComponent,
            onSubmit: (formValue: InventoryAdjustmentFormValue) => this.handleInventoryAdjustment(formValue),
            onCancel: () => this.handleHideSidebar(),
            inputs: {
              loading: this.adjustmentLoading(),
              error: this.adjustmentError(),
              unit: ingredient?.unit || ''
            }
          }
        ]
      }
    ];
  });
  sidebarTitle = computed(() => {
    const ingredient = this.ingredientsStore.selectedIngredient();
    return ingredient ? `Ingrediente: ${ingredient.name}` : 'Detalles del Ingrediente';
  });
  sidebarSubtitle = computed(() => {
    const ingredient = this.ingredientsStore.selectedIngredient();
    return ingredient ? `UUID: ${ingredient.uuid}` : '';
  });
  private readonly router = inject(Router);
  // TODO EVALUAR SI MOVER A STORE
  actions: ActionButtonInfo[] = [
    {
      action: 'edit',
      icon: 'pi pi-pencil',
      label: 'Edit',
      severity: 'info',
      onClick: (rowData) => this.router.navigate(['/ingredients/', rowData.uuid, 'edit'])
    },
    {
      action: 'delete', icon: 'pi pi-trash', label: 'Delete', severity: 'danger', onClick: (rowData) => {
      } /* TODO implementar eliminar ingrediente */
    },
  ];

  changePageHandler($event: { page: number; size: number }) {
    this.ingredientsStore.getIngredients($event);
  }

  handleIngredientSelected($event: string) {
    const uuid = $event;

    const selectedIng = this.ingredients().find(ing => ing.uuid === uuid);

    if (selectedIng) {
      this.selectedIngredient.set(selectedIng);
    }

    // Limpiar error antes de abrir el sidebar
    this.ingredientsStore.clearError();
    this.ingredientsStore.getIngredientById({uuid});
    this.sidebarVisible = true;
  }

  handleHideSidebar() {
    this.sidebarVisible = false;
    this.ingredientsStore.clearSelectedIngredient();
    this.selectedIngredient.set(null);
    // Limpiar error al cerrar el sidebar
    this.ingredientsStore.clearError();
  }

  handleInventoryAdjustment(formValue: InventoryAdjustmentFormValue) {
    const ingredient = this.ingredientsStore.selectedIngredient();

    if (!ingredient) {
      window.alert('❌ Error: No hay ingrediente seleccionado');
      return;
    }

    // Validar que solo se use el modo "replace" (el modo "adjust" no está implementado en backend)
    if (formValue.adjustmentType === 'adjust') {
      window.alert(
        '⚠️ MODO NO DISPONIBLE\n\n' +
        'El modo "Ajustar" (suma/resta) aún no está habilitado en el sistema.\n\n' +
        'Por favor, usa el modo "Reemplazar" para establecer el nuevo valor de stock.'
      );
      return;
    }

    // Suscribirse al Observable del store
    this.ingredientsStore.adjustIngredientStock(ingredient.uuid, {
      newStock: formValue.quantity,
      reason: formValue.reason
    }).subscribe({
      next: (responseUuid) => {
        console.log('✅ Stock ajustado exitosamente:', responseUuid);
        this.handleHideSidebar();
      },
      error: (error) => {
        console.error('❌ Error al ajustar stock:', error);
      }
    });
  }
}

