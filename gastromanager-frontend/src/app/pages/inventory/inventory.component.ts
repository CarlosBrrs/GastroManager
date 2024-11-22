import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {IngredientResponseDto} from "../../core/model/interfaces/IngredientResponseDto";
import {InventoryService} from "../../core/services/inventory/inventory.service";
import {finalize, Subject, takeUntil} from "rxjs";
import {RouterOutlet} from "@angular/router";
import {InventoryTableComponent} from "./inventory-table/inventory-table.component";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {IngredientRequestDto} from "../../core/model/interfaces/IngredientRequestDto";
import {IngredientStore} from "../../core/store/inventory/ingredient.store";

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
export class InventoryComponent implements OnInit, OnDestroy {

  ingredients = signal<IngredientResponseDto[]>([])
  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  private destroy$ = new Subject<void>();
  ingredientStore = inject(IngredientStore);

  constructor(private inventoryService: InventoryService, private messageService: MessageService/*, private confirmationService: ConfirmationService*/) {
  }

  ngOnInit(): void {
    this.loadIngredients();
    // this.ingredientStore
  }

  ngOnDestroy(): void {
    this.destroy$.next();  // trigger the unsubscribe
    this.destroy$.complete();
  }

  // object with payload  and uuid
  handleEdit(ingredient: { uuid: string, payload: Partial<IngredientRequestDto> }) {
    this.loading.set(true);
    this.inventoryService.updateIngredient(ingredient.uuid, ingredient.payload)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false))
      ).subscribe({
      next: (response) => {
        this.loadIngredients()
        this.messageService.add({
          severity: 'success',
          summary: 'Ingredient Updated',
          detail: 'Ingredient updated successfully.'
        });
        // this.loading.set(false);
      }
      ,
      error: error => {
        this.messageService.add({severity: 'error', summary: 'Error updating ingredient', detail: error.error.message});
        console.log("error updating ingredient", error)
        this.error.set('Error updating ingredient');
      },
      complete: () => {
        console.log("completed handle edit in inventory component")
      }
    })
  }

  handleDelete(ingredientUuid: string) {
    this.loading.set(true);

    this.inventoryService.deleteIngredient(ingredientUuid)
    this.messageService.add({severity: 'success', summary: 'Item deleted successfully', detail: ingredientUuid});
    this.loadIngredients(); // Recargar la lista después de borrar

  }

  handleAdd(ingredient: IngredientRequestDto) {
    this.loading.set(true);
    this.inventoryService.addIngredient(ingredient)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false)))
      .subscribe({
        next: response => {
          this.loadIngredients()
          this.messageService.add({
            severity: 'success',
            summary: 'Ingredient Added',
            detail: 'Ingredient added successfully.'
          });
        },
        error: error => {
          this.messageService.add({severity: 'error', summary: 'Error adding ingredient', detail: error.error.message});
          console.log("error adding ingredient", error)
          this.error.set('Error adding ingredient');
          console.log(error);
        },
        complete: () => {
          console.log("completed handle add in inventory component")
        }
      })
  }

  private loadIngredients() {
    this.loading.set(true)
    this.inventoryService.getAllIngredients()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false)))
      .subscribe({
        next: response => {
          this.ingredients.set(response.data);
          this.loading.set(false)
        },
        error: error => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error loading ingredients',
            detail: error.error.message
          });
          console.log("error loading ingredients", error)
          this.error.set('Error loading ingredients');
        },
        complete: () => {
          console.log("completed successfully")
        }
      })
  }
}
