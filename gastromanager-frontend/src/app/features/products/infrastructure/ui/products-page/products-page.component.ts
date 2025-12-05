import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {SidebarModule} from "primeng/sidebar";
import {ColumnProperties} from "../../../../../core/store/inventory/inventory.store";
import {Router} from "@angular/router";
import {ProductStore} from "../../../../../core/store/product/product.store";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {ProductsTableComponent} from "../products-table/products-table.component";

@Component({
  selector: 'gm-products-page',
  standalone: true,
  imports: [
    SidebarModule,
    ProductsTableComponent
  ],
  templateUrl: './products-page.component.html',
  styleUrl: './products-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductsPageComponent {

  private readonly productsStore = inject(ProductStore);
  products = computed(() => {
    const page = this.productsStore.currentPage();
    return this.productsStore.pages().get(page) || [];
  });
  totalRecords = computed(() => this.productsStore.totalRecords());
  loading = computed(() => this.productsStore.loading());
  selectedProduct = computed(() => this.productsStore.selectedProduct());
  tableColumns: ColumnProperties[] = this.productsStore.tableColumns();
  private readonly router = inject(Router);
  // TODO EVALUAR SI MOVER A STORE
  actions: ActionButtonInfo[] = [
    {
      action: 'edit',
      icon: 'pi pi-pencil',
      label: 'Edit',
      severity: 'info',
      onClick: (rowData) => this.router.navigate(['/products/', rowData.uuid, 'edit'])
    },
    {
      action: 'delete', icon: 'pi pi-trash', label: 'Delete', severity: 'danger', onClick: (rowData) => {
      } /* TODO implementar eliminar ingrediente */
    },
  ];

  handleProductSelected($event: any) {

  }

  changePageHandler($event: { page: number; size: number }) {
    this.productsStore.getProducts($event);
  }
}
