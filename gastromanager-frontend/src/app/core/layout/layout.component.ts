import {Component, effect, OnInit, signal} from '@angular/core';
import {SidebarComponent} from "./sidebar/sidebar.component";
import {RouterOutlet} from "@angular/router";
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
    JsonPipe
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

  constructor(private authService: AuthService, private userService: UserService) {
    this.isLoggedIn = this.authService.isLoggedIn;

    effect(() => {
      if (this.isLoggedIn()) {
        this.userInfo = this.userService.userInfo;
        console.log("validating role with token")
        console.log("the roles in the token are ", this.userInfo()?.roles?.toString())

        const infoFromUserService = this.userService.userInfo();
        if (infoFromUserService) {
          this.tokenRole.set(infoFromUserService.roles[0].name);
        }
        console.log("you are logged in, your info is", this.userInfo());

      } else {
        this.userService.userInfo.set(undefined)
        this.tokenRole.set("")
        console.log("you are logged off, your info is", this.userInfo());
      }
    }, {allowSignalWrites: true});
    // this.userService.loadUserInfo().subscribe(response => this.userInfo.set(response.data));
  }


  ngOnInit(): void {
  }

  onRestaurantSelection($event: any) {
    console.log("restaurant selected: " + $event.name);
  }
}
