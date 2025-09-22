import {Observable} from "rxjs";
import {Restaurant} from "../models/restaurant.interface";
import {UserRestaurantResponseDto} from "../models/user-restaurant-response-dto.interface";

export interface RestaurantsRepository {

  getRestaurantDetails(uuid: string): Observable<Restaurant>;
  getUserRestaurants(): Observable<UserRestaurantResponseDto[]>;

}
