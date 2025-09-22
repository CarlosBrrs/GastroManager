import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {RestaurantsRepository} from '../../domain/ports/restaurants.repository';
import {Restaurant} from '../../domain/models/restaurant.interface';
import {RestaurantsAdapter} from "../../infrastructure/api/restaurants.adapter";

@Injectable({
  providedIn: 'root'
})
export class GetRestaurantByUuidUseCase {
  private readonly restaurantsRepository: RestaurantsRepository = inject(RestaurantsAdapter);

  execute(uuid: string): Observable<Restaurant> {
    return this.restaurantsRepository.getRestaurantDetails(uuid);
  }
}
