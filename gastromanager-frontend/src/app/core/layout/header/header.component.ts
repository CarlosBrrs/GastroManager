import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ToolbarModule} from "primeng/toolbar";
import {Button} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {NgOptimizedImage} from "@angular/common";
import {AvatarModule} from "primeng/avatar";
import {OverlayPanelModule} from "primeng/overlaypanel";
import {RouterLink} from "@angular/router";
import {AuthService} from "../../services/auth/auth.service";

@Component({
  selector: 'gm-header',
  standalone: true,
  imports: [
    ToolbarModule,
    Button,
    DropdownModule,
    FormsModule,
    NgOptimizedImage,
    AvatarModule,
    OverlayPanelModule,
    RouterLink
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  @Input() role!: string; // Asigna este valor desde el componente padre
  @Input() restaurants: Array<{ name: string }> = []; // Lista de restaurantes
  @Input() assignedRestaurant!: { name: string }; // Restaurante asignado
  @Output() restaurantSelection: any = new EventEmitter();

  constructor(private authService: AuthService) {
  }

  onSelectRestaurant() {

    this.restaurantSelection.emit(this.assignedRestaurant);

  }

  toggleSidebar() {
    window.alert("to toggle sidebar")
  }

  logout() {
    this.authService.logout();
  }
}
