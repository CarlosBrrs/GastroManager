export interface IngredientDetailResponseDto {
  uuid: string;
  name: string;
  availableStock: number;
  unit: string;
  pricePerUnit: number;
  supplier: string;
  minimumStockQuantity: number;
  createdBy: string;
  createdDate: string;
  updatedBy: string;
  updatedDate: string;
}
