import {IngredientResponseDto} from "../../domain/models/ingredient-response-dto.interface";
import {Ingredient} from "../../domain/models/ingredient.interface";
import {IngredientRequestDto} from "../../domain/models/ingredient-request-dto.interface";
import {IngredientDetailResponseDto} from "../../domain/models/ingredient-detail-response-dto.interface";


export function mapToIngredientSummary(dto: IngredientResponseDto): Ingredient {
  return {
    uuid: dto.uuid,
    name: dto.name,
    pricePerUnit: dto.pricePerUnit,
    supplier: dto.supplier,
    unit: dto.unit,
    availableStock: dto.availableStock,
    minimumStockQuantity: dto.minimumStockQuantity,
    createdBy: dto.createdBy,
    createdDate: dto.createdDate,
    updatedBy: dto.updatedBy,
    updatedDate: dto.updatedDate,
  };
}

export function mapToIngredientDetail(dto: IngredientDetailResponseDto): Ingredient {
  return {
    uuid: dto.uuid,
    name: dto.name,
    pricePerUnit: dto.pricePerUnit,
    supplier: dto.supplier,
    unit: dto.unit,
    availableStock: dto.availableStock,
    minimumStockQuantity: dto.minimumStockQuantity,
    createdBy: dto.createdBy,
    createdDate: new Date(dto.createdDate),
    updatedBy: dto.updatedBy,
    updatedDate: new Date(dto.updatedDate)
  };
}

export function mapToIngredientRequestDto(ingredient: Ingredient): IngredientRequestDto {
  return {
    name: ingredient.name,
    pricePerUnit: ingredient.pricePerUnit,
    availableStock: ingredient.availableStock,
    unit: ingredient.unit,
    supplier: ingredient.supplier,
    minimumStockQuantity: ingredient.minimumStockQuantity
  };
}

export function mapToEditIngredientRequestDto(ingredient: Ingredient): IngredientRequestDto {
  return {
    name: ingredient.name,
    pricePerUnit: ingredient.pricePerUnit,
    unit: ingredient.unit,
    supplier: ingredient.supplier,
    minimumStockQuantity: ingredient.minimumStockQuantity
  };
}

