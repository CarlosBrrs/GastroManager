import {ProductItemIngredientResponseDto} from "./ProductItemIngredientResponseDto";

export interface ProductItemResponseDto {
  uuid: string
  name: string
  description: string
  category: string
  isEnabled: boolean
  price: number
  ingredients: ProductItemIngredientResponseDto[]
}
