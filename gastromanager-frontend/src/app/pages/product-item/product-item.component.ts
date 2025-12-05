import {Component, effect, inject, OnDestroy, OnInit} from '@angular/core';
import {ProductItemTableComponent} from "./product-item-table/product-item-table.component";
import {MessageService} from "primeng/api";
import {ProductItemStore} from "../../core/store/product-item/product-item.store";
import {StoreEventService} from "../../core/services/store-event/store-event.service";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'gm-product-item',
  standalone: true,
  imports: [
    ProductItemTableComponent
  ],
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.scss'
})
export class ProductItemComponent implements OnInit, OnDestroy {

  productItemStore = inject(ProductItemStore)
  private readonly destroy$ = new Subject<void>();

  constructor(private readonly messageService: MessageService, private readonly storeEventService: StoreEventService) {
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
    this.productItemStore.loadProductItems()
      .pipe(
        takeUntil(this.destroy$)
      ).subscribe();
  }

  handleAdd(productItem: any) {
    this.productItemStore.addProductItem(productItem).subscribe(productItems => {
      console.log("product item added, loading new product item list", productItems)
    });
  }

  handleEdit(productItem: any) {
    this.productItemStore.updateProductItem(productItem.uuid, productItem.payload).subscribe(productItems => {
      console.log("product item edited, loading new product item list", productItems)
    });
  }

  handleDelete(productItemUuid: number) {
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
