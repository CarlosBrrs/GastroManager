import { RestaurantConfigResponseDto } from './restaurant-config-response-dto.interface';

export interface RestaurantDetailResponseDto {
  uuid: string;
  name: string;
  description: string;
  address: string;
  ownerUuid: string;
  config: RestaurantConfigResponseDto;
}
