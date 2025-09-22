import {Component, effect, OnInit, signal} from '@angular/core';
import {SidebarComponent} from "./sidebar/sidebar.component";
import {RouterLink, RouterOutlet} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {HeaderComponent} from "./header/header.component";
import {FooterComponent} from "./footer/footer.component";
import {UserService} from "../services/users/user.service";
import {JsonPipe} from "@angular/common";
import {UserResponseDto} from "../../services/models/user-response-dto";

export const mockRestaurants = [
  {name: 'Restaurante Italiano'},
  {name: 'Sushi Bar'},
  {name: 'Café del Mar'},
  {name: 'Pizzería Gourmet'},
  {name: 'Tacos al Pastor'}
];

export const mockAssignedRestaurant = {name: 'Restaurante Italiano'};


@Component({
  selector: 'gm-layout',
  standalone: true,
  imports: [
    SidebarComponent,
    RouterOutlet,
    HeaderComponent,
    FooterComponent,
    JsonPipe,
    RouterLink,
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent implements OnInit {

  isLoggedIn = signal(false);
  tokenRole = signal("");
  userInfo = signal<UserResponseDto | undefined>(undefined)
  restaurants: Array<{ name: string }> = mockRestaurants; // Usar datos simulados
  assignedRestaurant: { name: string } = mockAssignedRestaurant; // Usar datos simulados

  constructor(private readonly authService: AuthService, private readonly userService: UserService) {
    this.isLoggedIn = this.authService.isLoggedIn;
    effect(() => {
      if (this.isLoggedIn()) {
        this.userInfo = this.userService.userInfo;
        const infoFromUserService = this.userService.userInfo();

        if (infoFromUserService) {
          this.tokenRole.set(this.authService.getRoles()[0]);
        }
      } else {
        this.userService.userInfo.set(undefined)
        this.tokenRole.set("")
      }
    }, {allowSignalWrites: true});
  }


  ngOnInit(): void {
    if (!this.userService.userInfo()) {
      this.userService.loadUserInfo().subscribe({
        next: () => console.log("User info loaded"),
        error: () => console.log("Failed to load user info")
      });
    }
  }

  onRestaurantSelection($event: any) {
    console.log("restaurant selected: " + $event.name);
  }
}
