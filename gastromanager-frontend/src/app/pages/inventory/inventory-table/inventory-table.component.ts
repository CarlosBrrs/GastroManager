import {Component, computed, EventEmitter, Input, Output, signal, ViewChild} from '@angular/core';
import {Table, TableModule, TableRowSelectEvent} from "primeng/table";
import {IngredientResponseDto} from "../../../core/model/interfaces/IngredientResponseDto";
import {CurrencyPipe, DatePipe} from "@angular/common";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {Button} from "primeng/button";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InventoryFormComponent} from "../inventory-form/inventory-form.component";
import {Ripple} from "primeng/ripple";
import {IngredientRequestDto} from "../../../core/model/interfaces/IngredientRequestDto";
import {SidebarModule} from "primeng/sidebar";
import {IngredientItem} from "../../../core/store/inventory/ingredient.model";

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
    Ripple,
    SidebarModule,
    DatePipe
  ],
  templateUrl: './inventory-table.component.html',
  styleUrl: './inventory-table.component.scss'
})
export class InventoryTableComponent {

  @Input() loading = false;
  @ViewChild('dt') table!: Table;

  isModalMaximized: boolean = false;
  isModalVisible: boolean = false;
  selectedIngredient?: IngredientResponseDto;
  @Output() edit = new EventEmitter<{ payload: IngredientRequestDto, uuid: string }>();
  @Output() delete = new EventEmitter<string>();
  @Output() addNew = new EventEmitter<IngredientRequestDto>();
  private ingredientList = signal<IngredientItem[]>([]);
  ingredients = computed(() => {
    return this.ingredientList();
  });

  // to receive the data from parent
  @Input()
  set data(value: IngredientItem[]) {
    this.ingredientList.set(value);
  }

  openNew() {
    this.selectedIngredient = undefined;
    this.isModalVisible = true;
  }

  hideDialog(): void {
    this.isModalVisible = false;
    this.selectedIngredient = undefined;  // Desseleccionamos el ingrediente
    this.table.clear();  // Limpiamos la selección en la tabla
  }

  editIngredient(ingredient: IngredientResponseDto) {
    this.selectedIngredient = ingredient;
    this.isModalVisible = true;
  }

  deleteIngredient(ingredientUuid: string) {
    this.delete.emit(ingredientUuid);
  }

  onGlobalFilter(dt: Table, event: Event) {
    dt.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  // TODO: CAMBIAR TIPO y separar el uuid del ingredient, ambos parametros estan llevando el uuid

  onFormSubmit(ingredient: IngredientRequestDto) {
    if (this.selectedIngredient) {
      const ingredientData = {...ingredient};
      delete ingredientData.availableStock;  // Quitar availableStock solo en edición
      this.edit.emit({payload: ingredientData, uuid: this.selectedIngredient.uuid});
    } else {
      this.addNew.emit(ingredient);
    }
    this.hideDialog();
  }

  isMaximized(event: any): boolean {
    this.isModalMaximized = event.maximized;
    return this.isModalMaximized;
  }

  onRowSelect($event: TableRowSelectEvent) {
    console.dir($event) //selected
    this.selectedIngredient = $event.data;
    this.openSidebar()
  }

  sidebarVisible: boolean = false;

  // Abrir el sidebar
  openSidebar() {
    this.sidebarVisible = true;
  }

  // Cerrar el sidebar
  closeSidebar() {
    this.sidebarVisible = false;
    this.selectedIngredient = undefined;  // Desseleccionamos el ingrediente
    this.table.clear();  // Limpiamos la selección en la tabla
  }

  displayInputNewStock() {

  }

  clearText() {
    alert("to clear text")
  }
}
