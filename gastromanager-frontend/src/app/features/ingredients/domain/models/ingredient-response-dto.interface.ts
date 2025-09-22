export interface IngredientResponseDto {
  uuid: string;
  name: string;
  unitPrice: number;
  availableStock: number;
  supplier: string;
  unit: string;
  pricePerUnit: number;
  minimumStockQuantity: number;
  createdBy: string;
  createdDate: Date;
  updatedBy: string;
  updatedDate: Date;
}
