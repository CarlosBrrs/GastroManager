import {Component, computed, EventEmitter, Input, Output, signal} from '@angular/core';
import {Button} from "primeng/button";
import {CurrencyPipe} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InventoryFormComponent} from "../../inventory/inventory-form/inventory-form.component";
import {PrimeTemplate} from "primeng/api";
import {Table, TableModule} from "primeng/table";
import {ToolbarModule} from "primeng/toolbar";
import {ProductItemResponseDto} from "../../../core/model/interfaces/ProductItemResponseDto";
import {ProductItemFormComponent} from "../product-item-form/product-item-form.component";

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
    ProductItemFormComponent
  ],
  templateUrl: './product-item-table.component.html',
  styleUrl: './product-item-table.component.scss'
})
export class ProductItemTableComponent {

  visibleModal: boolean = false;
  maximizeModal: boolean = false;
  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<number>();
  @Output() addNew = new EventEmitter<any>();
  selectedProductItem: any;
  private _productItems = signal<ProductItemResponseDto[]>([]);
  productItems = computed(() => {
    return this._productItems();
  });

  @Input()
  set data(value: ProductItemResponseDto[]) {
    this._productItems.set(value);
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
}
