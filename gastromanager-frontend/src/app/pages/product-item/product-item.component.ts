import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {ProductItemService} from "../../core/services/product-item/product-item.service";
import {InventoryTableComponent} from "../inventory/inventory-table/inventory-table.component";
import {ProductItemTableComponent} from "./product-item-table/product-item-table.component";
import {finalize, Subject, takeUntil} from "rxjs";
import {MessageService} from "primeng/api";
import {ProductItemStore} from "../../core/store/product-item/product-item.store";
import {StoreEventService} from "../../core/services/store-event/store-event.service";
import {ProductItem} from "../../core/store/product-item/product-item.model";

@Component({
  selector: 'gm-product-item',
  standalone: true,
  imports: [
    InventoryTableComponent,
    ProductItemTableComponent
  ],
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.scss'
})
export class ProductItemComponent implements OnInit {

  productItems = signal<ProductItem[]>([]).asReadonly();
  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  productItemStore = inject(ProductItemStore)

  constructor(private productItemService: ProductItemService, private messageService: MessageService, private storeEventService: StoreEventService) {
    // Efecto para manejar eventos de éxito
    effect(() => {
      const successMessage = this.storeEventService.successSignal();
      const successHeaderMessage = this.storeEventService.successHeaderSignal();
      if (successMessage) {
        this.messageService.add({
          severity: 'success',
          summary: successHeaderMessage,
          detail: successMessage
        });
        // Opcional: limpiar el mensaje después de mostrarlo
        this.storeEventService.successSignal.set(null);
        this.storeEventService.successHeaderSignal.set(undefined);
      }
    }, {allowSignalWrites: true});

    // Efecto para manejar eventos de error
    effect(() => {
      const errorMessage = this.storeEventService.errorSignal();
      const errorHeaderMessage = this.storeEventService.errorHeaderSignal();
      if (errorMessage) {
        this.messageService.add({
          severity: 'error',
          summary: errorHeaderMessage,
          detail: errorMessage
        });
        // Opcional: limpiar el mensaje después de mostrarlo
        this.storeEventService.errorSignal.set(null);
        this.storeEventService.errorHeaderSignal.set(undefined);
      }
    }, {allowSignalWrites: true});
  }

  ngOnInit(): void {
    this.loadProductItems();
  }

  handleAdd(productItem: any) {
    this.productItemStore.addProductItem(productItem);
  }

  handleEdit(productItem: any) {
    // this.loading.set(true);
    this.productItemStore.editProductItem(productItem.uuid, productItem.payload);
  }

  handleDelete(productItemUuid: number) {
  }

  private loadProductItems() {
    this.loading.set(true)
    this.productItems = this.productItemStore.productItems;
  }
}
