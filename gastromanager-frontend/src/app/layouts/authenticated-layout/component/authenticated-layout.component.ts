import {Component, computed, inject, signal} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {NavbarComponent} from "../../../shared/components/navbar/navbar.component";
import {PrimarySidebarComponent} from "../../../shared/components/primary-sidebar/primary-sidebar.component";
import {SecondarySidebarComponent} from "../../../shared/components/secondary-sidebar/secondary-sidebar.component";
import {ContentHeaderComponent} from "../../../shared/components/content-header/content-header.component";
import {LayoutStore} from "../store/authenticated-layout.store";

@Component({
  selector: 'gm-authenticated-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    NavbarComponent,
    PrimarySidebarComponent,
    SecondarySidebarComponent,
    ContentHeaderComponent
  ],
  templateUrl: './authenticated-layout.component.html',
  styleUrl: './authenticated-layout.component.scss'
})
export class AuthenticatedLayoutComponent {

  layoutStore = inject(LayoutStore);
  showSecondarySidebar = computed(() => this.layoutStore.showSecondarySidebar());
  isPrimaryOpen = signal(false);
  isSecondaryOpen = signal(false);


  toggleSecondary() {
    this.isSecondaryOpen.update(v => !v);
  }

  togglePrimary() {
    this.isPrimaryOpen.update(v => !v);
    if (!this.isPrimaryOpen()) this.isSecondaryOpen.set(false);
  }

}
