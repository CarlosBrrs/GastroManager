import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'gm-primary-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    NgOptimizedImage
  ],
  templateUrl: './primary-sidebar.component.html',
  styleUrl: './primary-sidebar.component.scss'
})
export class PrimarySidebarComponent {
  links = [
    {label: 'Dashboard', route: '/dashboard'},
    {label: 'Ingredientes', route: '/ingredients'},
    {label: 'Menús', route: '/menus'},
    {label: 'Productos', route: '/products'},
    {label: 'Ordenes', route: '/orders'},
    {label: 'Reportes', route: '/reports'},
    {label: 'Administración', route: '/management'},

  ];
}
