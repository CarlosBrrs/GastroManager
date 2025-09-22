import {Component, computed, inject, Input} from '@angular/core';
import {RouterLink} from "@angular/router";
import {LayoutStore} from "../../../layouts/authenticated-layout/store/authenticated-layout.store";

@Component({
  selector: 'gm-secondary-sidebar',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './secondary-sidebar.component.html',
  styleUrl: './secondary-sidebar.component.scss'
})
export class SecondarySidebarComponent {
  layoutStore = inject(LayoutStore);
  links = computed(() => this.layoutStore.secondarySidebarLinks());
}
