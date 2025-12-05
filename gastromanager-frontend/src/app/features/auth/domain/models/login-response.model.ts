import {RestaurantResponseDto} from './restaurant-response-dto.interface';

export interface LoginResponse {
  jwtToken: string;
  restaurants: RestaurantResponseDto[];
}
