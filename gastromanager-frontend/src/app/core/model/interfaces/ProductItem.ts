import {ProductItemIngredient} from "./ProductItemIngredient";

export interface ProductItem {
  uuid: string;
  name: string;
  description: string;
  category: string;
  isEnabled: boolean;
  price: number;
  ingredients: ProductItemIngredient[];
}
