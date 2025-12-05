import {ChangeDetectionStrategy, Component, computed, inject} from '@angular/core';
import {AuthStore} from "../../../../../core/store/auth/auth.store";

@Component({
  selector: 'gm-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardComponent {

  authStore = inject(AuthStore)
  restaurant = computed(() => this.authStore.selectedRestaurant());

}
