export interface IngredientResponseDto {
  supplier: string;
  createdDate: string;
  createdBy: string;
  uuid: string;
  name: string;
  availableStock: number;
  unit: string;
  pricePerUnit: number;
  minimumStockQuantity: number;
  // updatedDate: string;
}
