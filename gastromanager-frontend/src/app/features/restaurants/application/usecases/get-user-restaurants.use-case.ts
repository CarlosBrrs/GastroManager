import { inject, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { UserRestaurantResponseDto } from '../../domain/models/user-restaurant-response-dto.interface';
import { RestaurantsRepository } from '../../domain/ports/restaurants.repository';
import {RestaurantsAdapter} from "../../infrastructure/api/restaurants.adapter";

@Injectable({
  providedIn: 'root'
})
export class GetUserRestaurantsUseCase {
  private readonly restaurantsRepository: RestaurantsRepository = inject(RestaurantsAdapter);

  execute(): Observable<UserRestaurantResponseDto[]> {
    return this.restaurantsRepository.getUserRestaurants();
  }
}
