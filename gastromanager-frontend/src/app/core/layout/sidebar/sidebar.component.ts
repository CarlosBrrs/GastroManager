import {Component} from '@angular/core';
import {AuthService} from "../../services/auth/auth.service";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'gm-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

  auth: AuthService;

  constructor(private authService: AuthService, private router: Router) {
    this.auth = authService;
  }

  logoutHandle(): void {
    this.authService.logout();
  }
}
