export interface IngredientRequestDto {
  name: string;
  availableStock?: number;
  unit: string;
  supplier: string;
  pricePerUnit: number;
  minimumStockQuantity: number;
}
