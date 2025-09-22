import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {AuthStore} from "../../../../../core/store/auth/auth.store";
import {JsonPipe} from "@angular/common";

@Component({
  selector: 'gm-dashboard',
  standalone: true,
  imports: [
    JsonPipe
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardComponent {

  authStore = inject(AuthStore)
  restaurant = computed(() => this.authStore.selectedRestaurant());

}
