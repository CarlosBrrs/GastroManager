import { Restaurant } from '../../domain/models/restaurant.interface';
import { RestaurantDetailResponseDto } from '../../domain/models/restaurant-detail-response-dto.interface';

export function mapToRestaurantDetail(dto: RestaurantDetailResponseDto): Restaurant {
  return {
    uuid: dto.uuid,
    name: dto.name,
    address: dto.address,
    configs: {
      uuid: dto.config.uuid,
      requiresPaymentBeforeOrder: dto.config.requirePaymentBeforeOrder
    }
  };
}
