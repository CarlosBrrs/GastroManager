import {RestaurantConfig} from "./restaurant-config.interface";

export interface Restaurant {
  uuid?: string;
  name: string;
  address: string;
  configs: RestaurantConfig;
}
