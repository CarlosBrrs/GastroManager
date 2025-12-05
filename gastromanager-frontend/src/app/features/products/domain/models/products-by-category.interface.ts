import {Product} from "./product.interface";

export interface ProductsByCategory {
  [category: string]: Product[];
}
