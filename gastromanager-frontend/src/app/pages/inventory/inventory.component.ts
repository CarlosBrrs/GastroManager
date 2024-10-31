import {Component, OnDestroy, OnInit, signal} from '@angular/core';
import {IngredientResponseDto} from "../../core/model/interfaces/IngredientResponseDto";
import {InventoryService} from "../../core/services/inventory/inventory.service";
import {finalize, Subject, takeUntil} from "rxjs";
import {Router, RouterOutlet} from "@angular/router";
import {InventoryTableComponent} from "./inventory-table/inventory-table.component";

@Component({
  selector: 'gm-inventory',
  standalone: true,
  imports: [
    RouterOutlet,
    InventoryTableComponent,
    // ToastModule,
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss'
})
export class InventoryComponent implements OnInit, OnDestroy {

  ingredients = signal<IngredientResponseDto[]>([])
  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  private destroy$ = new Subject<void>();

  // showForm = false;

  constructor(private inventoryService: InventoryService, private router: Router

              /*, private messageService: MessageService, private confirmationService: ConfirmationService*/) {
  }

  ngOnInit(): void {
    this.loadIngredients();

  }


  ngOnDestroy(): void {
    this.destroy$.next();  // trigger the unsubscribe
    this.destroy$.complete();
  }

  // object with payload  and uuid
  handleEdit(ingredient: any) {
    this.loading.set(true);
    this.inventoryService.updateIngredient(ingredient.uuid, ingredient.payload)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false))
      ).subscribe(response => {
      this.loadIngredients()
      this.loading.set(false);
    })
  }

  handleDelete(ingredientUuid: string) {
    this.loading.set(true);
    this.inventoryService.deleteIngredient(ingredientUuid)

  }

  handleAdd(ingredient: any) {
    this.loading.set(true);
    this.inventoryService.addIngredient(ingredient)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false)))
      .subscribe(response => {
          this.loadIngredients()
          this.loading.set(false);
        }, error => {
          this.error.set('Error updating ingredients');
          console.log(error);
          // this.messageService.add({
          //   severity: 'error',
          //   summary: 'Error',
          //   detail: 'Failed to load ingredients'
          // });
        }
      );

  }

  private loadIngredients() {
    this.loading.set(true)
    this.inventoryService.getAllIngredients()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading.set(false)))
      .subscribe(response => {
        this.ingredients.set(response.data);
        this.loading.set(false)
      }, error => {
        this.error.set('Error loading ingredients');
        // this.messageService.add({
        //   severity: 'error',
        //   summary: 'Error',
        //   detail: 'Failed to load ingredients'
        // });
      })
  }
}
