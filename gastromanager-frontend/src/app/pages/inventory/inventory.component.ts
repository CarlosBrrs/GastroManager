import {Component, effect, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {AdjustStockRequestDto, InventoryTableComponent} from "./inventory-table/inventory-table.component";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {IngredientRequestDto} from "../../core/model/interfaces/IngredientRequestDto";
import {IngredientStore} from "../../core/store/inventory/ingredient.store";
import {StoreEventService} from "../../core/services/store-event/store-event.service";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'gm-inventory',
  standalone: true,
  imports: [
    RouterOutlet,
    InventoryTableComponent,
    ToastModule,
    JsonPipe,
    AsyncPipe,
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss'
})
export class InventoryComponent implements OnInit, OnDestroy {

  ingredientStore = inject(IngredientStore);
  private readonly destroy$ = new Subject<void>();


  constructor(private readonly messageService: MessageService,
              private readonly storeEventService: StoreEventService,) {
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
    this.ingredientStore.loadIngredients()
      .pipe(
        takeUntil(this.destroy$)
      )
      .subscribe(ingredients => {
      console.log("ingredientes cargados ", ingredients)
    })
  }

  // object with payload  and uuid
  handleEdit(ingredient: { uuid: string, payload: IngredientRequestDto }) {
    this.ingredientStore.editIngredient(ingredient.uuid, ingredient.payload).subscribe(ingredients => {
      console.log("ingrediente editado y lista actualizada", ingredients)
    })
  }

  handleAdd(ingredient: IngredientRequestDto) {
    this.ingredientStore.addIngredient(ingredient).subscribe(ingredients => {
      console.log("ingrediente agregado y lista actualizada", ingredients)
    })
  }

  handleAdjustStock(ingredientStockAdjust: { uuid: string, payload: AdjustStockRequestDto }) {
    this.ingredientStore.adjustStock(ingredientStockAdjust.uuid, ingredientStockAdjust.payload).subscribe(ingredients => {
      console.log("Stock actualizado y lista actualizada", ingredients)
    })
  }

  handleDelete(ingredientUuid: string) {
    // this.loading.set(true);
    // this.ingredientStore.deleteIngredient(ingredientUuid);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
