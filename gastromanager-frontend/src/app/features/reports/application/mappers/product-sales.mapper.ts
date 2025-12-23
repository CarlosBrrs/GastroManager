import {ProductSales} from "../../domain/models/product-sales.interface";
import {ProductSalesResponseDto} from "../../domain/models/product-sales-response-dto.interface";

export function mapToProductSales(dto: ProductSalesResponseDto): ProductSales {
  return {
    productUuid: dto.productUuid,
    productName: dto.productName,
    unitsSold: dto.unitsSold,
    totalSales: dto.totalSales,
    salesPercentage: dto.salesPercentage
  };
}

export function mapToProductSalesList(dtos: ProductSalesResponseDto[]): ProductSales[] {
  return dtos.map(dto => mapToProductSales(dto));
}

