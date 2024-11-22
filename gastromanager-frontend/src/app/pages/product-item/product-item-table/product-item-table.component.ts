import {Component, computed, EventEmitter, Input, Output, signal, ViewChild} from '@angular/core';
import {Button} from "primeng/button";
import {CurrencyPipe, DatePipe, JsonPipe} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InventoryFormComponent} from "../../inventory/inventory-form/inventory-form.component";
import {ConfirmationService, MessageService, PrimeTemplate} from "primeng/api";
import {Table, TableModule, TableRowSelectEvent} from "primeng/table";
import {ToolbarModule} from "primeng/toolbar";
import {ProductItemResponseDto} from "../../../core/model/interfaces/ProductItemResponseDto";
import {ProductItemFormComponent} from "../product-item-form/product-item-form.component";
import {CheckboxChangeEvent, CheckboxModule} from "primeng/checkbox";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {FormsModule} from "@angular/forms";
import {SidebarModule} from "primeng/sidebar";

@Component({
  selector: 'gm-product-item-table',
  standalone: true,
  imports: [
    Button,
    CurrencyPipe,
    DialogModule,
    InputTextModule,
    InventoryFormComponent,
    PrimeTemplate,
    TableModule,
    ToolbarModule,
    ProductItemFormComponent,
    CheckboxModule,
    ConfirmDialogModule,
    FormsModule,
    DatePipe,
    SidebarModule,
    JsonPipe
  ],
  templateUrl: './product-item-table.component.html',
  styleUrl: './product-item-table.component.scss'
})
export class ProductItemTableComponent {

  @Input() loading = false;
  @ViewChild('dt') table!: Table;
  visibleModal: boolean = false;
  maximizeModal: boolean = false;
  selectedProductItem?: ProductItemResponseDto;
  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<number>();
  @Output() addNew = new EventEmitter<any>();
  @Output() productStatus = new EventEmitter<boolean>();
  private _productItemList = signal<ProductItemResponseDto[]>([]);
  productItems = computed(() => {
    return this._productItemList();
  });
  sidebarVisible: boolean = false;

  @Input()
  set data(value: ProductItemResponseDto[]) {
    this._productItemList.set(value);
  }

  constructor(
    private confirmationService: ConfirmationService, // Inyectamos ConfirmationService
    private messageService: MessageService // Inyectamos MessageService para notificaciones
  ) {
  }

  openNew() {
    this.selectedProductItem = undefined;
    this.visibleModal = true;
  }

  onGlobalFilter(dt: Table, $event: Event) {
    dt.filterGlobal(($event.target as HTMLInputElement).value, 'contains');
  }

  hideDialog() {
    this.visibleModal = false;
    this.selectedProductItem = undefined;  // Desseleccionamos el ingrediente
    this.table.clear();  // Limpiamos la selección en la tabla
  }

  editProductItem(productItem: any) {
    this.selectedProductItem = productItem;
    this.visibleModal = true;
  }

  isMaximized($event: any): boolean {
    this.maximizeModal = $event.maximized;
    return this.maximizeModal;
  }

  // work with defined models and interfaces
  onFormSubmit(productItem: any) {
    if (this.selectedProductItem) {
      this.edit.emit({payload: productItem, uuid: this.selectedProductItem.uuid});
    } else {
      this.addNew.emit(productItem);
    }
    this.hideDialog();
  }

  deleteProductItem(uuid: any): void {
    if (window.confirm("Are you sure you want to delete this product?")) {
      window.alert("item deleted with uuid " + uuid);
    }
  }


  onToggleEnableConfirm(productItem: any, event: CheckboxChangeEvent): void {
    const originalStatus = productItem.isEnabled; // Guarda el estado original del producto
    event.originalEvent?.preventDefault()
    // Muestra la confirmación antes de actualizar el estado
    this.confirmationService.confirm({
      message: `¿Estás seguro de que quieres ${originalStatus ? 'desactivar' : 'activar'} este producto?`,
      header: 'Confirmar Cambio',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        // Si el usuario confirma, emitimos el cambio al backend
        this.onToggleEnable(productItem.uuid, !originalStatus);
      },
      reject: () => {
        // Si el usuario cancela, revertimos el cambio del checkbox
        productItem.isEnabled = originalStatus;
      }
    });
  }

  onToggleEnable(uuid: string, isEnabled: boolean): void {
    // Aquí actualizamos el backend con el nuevo estado
    alert(`Cambio de estado emitido para el producto con UUID ${uuid}, nuevo estado: ${isEnabled ? 'habilitado' : 'deshabilitado'}`);

    /* this.productService.updateProductStatus(uuid, isEnabled).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Estado del producto actualizado correctamente.'
        });
      },
      error: (error) => {
        console.error('Error al actualizar el estado del producto:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Hubo un error al actualizar el estado del producto.'
        });
      }
    }); */
  }

  onRowSelect($event: TableRowSelectEvent) {
    console.log("i am heere")
    console.dir($event) //selected
    this.selectedProductItem = $event.data;
    this.openSidebar()
  }

  private openSidebar() {
    this.sidebarVisible = true;
  }

  // Cerrar el sidebar
  closeSidebar() {
    this.sidebarVisible = false;
    this.selectedProductItem = undefined;  // Desseleccionamos el ingrediente
    this.table.clear();  // Limpiamos la selección en la tabla
  }
}
