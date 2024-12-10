import {Component, effect, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {InventoryService} from "../../core/services/inventory/inventory.service";
import {Subject} from "rxjs";
import {RouterOutlet} from "@angular/router";
import {InventoryTableComponent} from "./inventory-table/inventory-table.component";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {IngredientRequestDto} from "../../core/model/interfaces/IngredientRequestDto";
import {IngredientStore} from "../../core/store/inventory/ingredient.store";
import {IngredientItem} from "../../core/store/inventory/ingredient.model";
import {StoreEventService} from "../../core/services/store-event/store-event.service";

@Component({
  selector: 'gm-inventory',
  standalone: true,
  imports: [
    RouterOutlet,
    InventoryTableComponent,
    ToastModule,
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss'
})
export class InventoryComponent implements OnInit {

  ingredients = signal<IngredientItem[]>([]).asReadonly()
  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  ingredientStore = inject(IngredientStore);

  constructor(private inventoryService: InventoryService, private messageService: MessageService, private storeEventService: StoreEventService,) {
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
    this.loadIngredients()
  }

  // object with payload  and uuid
  handleEdit(ingredient: { uuid: string, payload: Partial<IngredientRequestDto> }) {
    this.loading.set(true);
    this.ingredientStore.editIngredient(ingredient.uuid, ingredient.payload)
  }

  handleAdd(ingredient: IngredientRequestDto) {
    // this.loading.set(true);
    this.ingredientStore.addIngredient(ingredient);
  }

  handleDelete(ingredientUuid: string) {
    // this.loading.set(true);
    this.ingredientStore.deleteIngredient(ingredientUuid);
  }

  private loadIngredients() {
    this.loading.set(true)
    this.ingredients = this.ingredientStore.ingredients;
  }
}
