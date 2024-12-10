import {ProductItemIngredient} from "../../model/interfaces/ProductItemIngredient";

export type ProductItem = {
  uuid: string;
  name: string;
  description: string;
  price: number;
  ingredients: ProductItemIngredient[]
  category: string;
  isEnabled: boolean;
  createdBy: string;
  createdDate: string;
  updatedBy: string;
  updatedDate: string;
}
