import {Component, computed, inject} from '@angular/core';
import {AuthStore} from "../../../core/store/auth/auth.store";
import {NgOptimizedImage} from "@angular/common";
import {Router} from "@angular/router";
import {RestaurantStore} from "../../../core/store/restaurant/restaurant.store";

@Component({
  selector: 'gm-navbar',
  standalone: true,
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

  authStore = inject(AuthStore)
  router = inject(Router)
  user = computed(() => this.authStore.user())
  restaurantStore = inject(RestaurantStore)
  allowedRestaurants = this.restaurantStore.userRestaurants

  logout() {
    this.authStore.logout()
    this.router.navigate(['/login'])
  }

  changeSelectedRestaurant($event: Event) {
    const select = $event.target as HTMLSelectElement
    const uuid = select.value
    this.restaurantStore.selectRestaurant(uuid)
  }

  togglePrimary() {

  }
}
