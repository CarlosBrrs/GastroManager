export interface IngredientRequestDto {
  uuid?: string;
  name: string;
  pricePerUnit: number;
  supplier: string;
  unit: string;
  availableStock?: number;
  minimumStockQuantity?: number;
}
