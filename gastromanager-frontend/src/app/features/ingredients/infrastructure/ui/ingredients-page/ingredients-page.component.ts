import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {IngredientsTableComponent} from "../ingredients-table/ingredients-table.component";
import {ContentHeaderComponent} from "../../../../../shared/components/content-header/content-header.component";
import {PaginationComponent} from "../../../../../shared/components/pagination/pagination.component";
import {CurrencyPipe, JsonPipe} from "@angular/common";
import {ColumnProperties, InventoryStore} from "../../../../../core/store/inventory/inventory.store";
import {Router, RouterOutlet} from "@angular/router";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {SidebarModule} from "primeng/sidebar";

@Component({
  selector: 'gm-ingredients-page',
  standalone: true,
  imports: [
    IngredientsTableComponent,
    ContentHeaderComponent,
    PaginationComponent,
    CurrencyPipe,
    RouterOutlet,
    SidebarModule,
    JsonPipe,
  ],
  templateUrl: './ingredients-page.component.html',
  styleUrl: './ingredients-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IngredientsPageComponent {

  private readonly ingredientsStore = inject(InventoryStore);
  private readonly router = inject(Router);
  ingredients = computed(() => {
    const page = this.ingredientsStore.currentPage();
    return this.ingredientsStore.pages().get(page) || [];
  });
  totalRecords = computed(() => this.ingredientsStore.totalRecords());
  loading = computed(() => this.ingredientsStore.loading());
  selectedIngredient = computed(() => this.ingredientsStore.selectedIngredient());
  tableColumns: ColumnProperties[] = this.ingredientsStore.tableColumns();
  sidebarVisible: boolean = false;

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
    this.ingredientsStore.getIngredientById({uuid})
    this.sidebarVisible = true;
  }

  handleHideSidebar() {
    this.sidebarVisible = false;
    this.ingredientsStore.clearSelectedIngredient();
  }
}

