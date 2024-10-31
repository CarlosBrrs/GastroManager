import {IngredientRequestDto} from "./IngredientRequestDto";

export interface ProductItemRequestDto {
  name: string,
  description: string,
  price: number,
  category: string,
  ingredients: IngredientRequestDto[]
}
