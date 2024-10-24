import {Component, WritableSignal} from '@angular/core';
import {SidebarComponent} from "./sidebar/sidebar.component";
import {RouterOutlet} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'gm-layout',
  standalone: true,
  imports: [
    SidebarComponent,
    RouterOutlet
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

  isLoggedIn:  WritableSignal<boolean>;

  constructor(private authService: AuthService) {
    this.isLoggedIn = this.authService.isLoggedIn;
  }
}
