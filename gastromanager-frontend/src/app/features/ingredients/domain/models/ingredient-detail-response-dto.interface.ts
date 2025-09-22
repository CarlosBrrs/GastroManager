export interface IngredientDetailResponseDto {
  uuid: string; // UUID como string en TS
  name: string;
  availableStock: number;
  unit: string;
  pricePerUnit: number;
  supplier: string;
  minimumStockQuantity: number;
  createdBy: string;
  createdDate: string; // Instant en Java se representa como string (ISO 8601) en TS
  updatedBy: string;
  updatedDate: string;
}
