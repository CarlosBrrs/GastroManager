export interface IngredientUpdateRequestDto {
  name: string;
  unit: string;
  supplier: string;
  pricePerUnit: number;
  minimumStockQuantity: number;
}
