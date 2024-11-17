import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth/auth.service";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MenuModule} from "primeng/menu";
import {MenuItem} from "primeng/api";
import {SidebarService} from "../../services/sidebar/sidebar.service";

@Component({
  selector: 'gm-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    MenuModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {

  auth: AuthService;
  items: MenuItem[];

  constructor(private authService: AuthService, private sidebarService: SidebarService) {
    this.auth = authService;
    this.items = []
    this.sidebarService.getMenuItems()
  }

  ngOnInit(): void {
    this.items = [
      {
        label: 'Home',
        icon: 'pi pi-home',
        route: '/home',
        routerLinkActiveOptions: {exact: true},
      }, {
        label: 'Inventory',
        icon: 'pi pi-warehouse',
        route: '/inventory',
        routerLinkActiveOptions: {exact: true},
      }, {
        label: 'Products',
        icon: 'pi pi-sparkles',
        route: '/product-items',
        routerLinkActiveOptions: {exact: true},
      }]

    // Verificar roles específicos para mostrar items adicionales
    if (this.authService.hasAnyRole(['ROLE_SUPERUSER', 'ROLE_OWNER', 'ROLE_MANAGER'])) {
      this.items.push({
        label: 'Employees',
        icon: 'pi pi-users',
        route: '/employees',
        routerLinkActiveOptions: {exact: true},
      });
    }

    this.items.push({
      label: 'Orders',
      icon: 'pi pi-shopping-cart',
      route: '/orders',
      routerLinkActiveOptions: {exact: true},
    });
    this.items.push({
      label: 'Logout',
      icon: 'pi pi-sign-out',
      command: () => this.logout(),
      // routerLinkActiveOptions: {exact: true},
    });


  }

  logout() {
    this.authService.logout();
  }
}
