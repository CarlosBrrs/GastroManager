import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ToolbarModule} from "primeng/toolbar";
import {Button} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'gm-header',
  standalone: true,
  imports: [
    ToolbarModule,
    Button,
    DropdownModule,
    FormsModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  @Input() role!: string; // Asigna este valor desde el componente padre
  @Input() restaurants: Array<{ name: string }> = []; // Lista de restaurantes
  @Input() assignedRestaurant!: { name: string }; // Restaurante asignado
  @Output() restaurantSelection: any = new EventEmitter();


  onSelectRestaurant() {

    this.restaurantSelection.emit(this.assignedRestaurant);

  }

  toggleSidebar() {
    window.alert("to toggle sidebar")
  }
}
