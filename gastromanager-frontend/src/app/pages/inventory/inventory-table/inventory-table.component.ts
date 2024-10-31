import {Component, computed, EventEmitter, Input, Output, signal, ViewChild} from '@angular/core';
import {Table, TableModule, TableRowSelectEvent} from "primeng/table";
import {IngredientResponseDto} from "../../../core/model/interfaces/IngredientResponseDto";
import {CurrencyPipe} from "@angular/common";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {Button} from "primeng/button";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InventoryFormComponent} from "../inventory-form/inventory-form.component";
import {Ripple} from "primeng/ripple";

@Component({
  selector: 'gm-inventory-table',
  standalone: true,
  imports: [
    TableModule,
    CurrencyPipe,
    ToastModule,
    ToolbarModule,
    Button,
    ConfirmDialogModule,
    DialogModule,
    InputTextModule,
    InventoryFormComponent,
    Ripple
  ],
  templateUrl: './inventory-table.component.html',
  styleUrl: './inventory-table.component.scss'
})
export class InventoryTableComponent {

  @Input() loading = false;
  @ViewChild('dt') table!: Table;
  @ViewChild('inventoryForm') inventoryForm!: InventoryFormComponent;

  maximizeModal: boolean = false;
  visibleModal: boolean = false;
  // @Input() selectedIngredients: IngredientResponseDto[] = [];
  selectedIngredient?: IngredientResponseDto;
  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<string>();
  @Output() addNew = new EventEmitter<void>();
  private _ingredients = signal<IngredientResponseDto[]>([]);
  // Computed signal para filtrado
  ingredients = computed(() => {
    return this._ingredients();
  });

  // to receive the data from parent
  @Input()
  set data(value: IngredientResponseDto[]) {
    this._ingredients.set(value);
  }

  openNew() {
    this.selectedIngredient = undefined;
    this.visibleModal = true;
  }

  hideDialog(): void {
    this.visibleModal = false;
  }

  editIngredient(ingredient: IngredientResponseDto) {
    this.selectedIngredient = ingredient;
    this.visibleModal = true;
  }

  deleteIngredient(ingredientUuid: string) {
    this.delete.emit(ingredientUuid);
  }

  onGlobalFilter(dt: Table, event: Event) {
    dt.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  // TODO: CAMBIAR TIPO y separar el uuid del ingredient, ambos parametros estan llevando el uuid

  // request ing without uuid and emit an object with the payload and the uuid
  onFormSubmit(ingredient: any) {
    if (this.selectedIngredient) {
      this.edit.emit({payload: ingredient, uuid: this.selectedIngredient.uuid});
    } else {
      this.addNew.emit(ingredient);
    }
    this.hideDialog();
  }

  isMaximized(event: any): boolean {
    this.maximizeModal = event.maximized;
    return this.maximizeModal;
  }

/*  onRowSelect($event: TableRowSelectEvent) {
    console.dir($event) //selected
  }*/
}
