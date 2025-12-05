import {SalesReportFilters} from "../../domain/models/sales-report.interface";
import {
  OverviewSalesReport,
  OverviewSalesReportSummary,
  SalesReportFiltersInfo
} from "../../domain/models/overview-sales-report.interface";
import {
  OverviewSalesReportResponseDto,
  SalesReportFiltersInfoDto,
  SalesReportSummaryDto
} from "../../domain/models/overview-sales-report-response-dto.interface";

/**
 * Mapea la respuesta DTO del backend a la entidad de dominio OverviewSalesReport
 */
export function mapToOverviewSalesReport(dto: OverviewSalesReportResponseDto): OverviewSalesReport {
  return {
    filters: mapToSalesReportFiltersInfo(dto.filters),
    summary: mapToOverviewSalesReportSummary(dto.summary)
  };
}

/**
 * Mapea los filtros de dominio a query params para el backend
 */
export function mapToOverviewSalesReportFiltersDto(filters: SalesReportFilters): { dateFrom: string; dateTo: string } {
  return {
    dateFrom: filters.dateFrom.toISOString(),
    dateTo: filters.dateTo.toISOString()
  };
}

function mapToSalesReportFiltersInfo(dto: SalesReportFiltersInfoDto): SalesReportFiltersInfo {
  return {
    startDate: new Date(dto.startDate),
    endDate: new Date(dto.endDate),
    totalDays: dto.totalDays
  };
}

function mapToOverviewSalesReportSummary(dto: SalesReportSummaryDto): OverviewSalesReportSummary {
  return {
    totalOrders: dto.totalOrders,
    completedOrders: dto.completedOrders,
    cancelledOrders: dto.cancelledOrders,
    cancellationRate: dto.cancellationRate,
    totalOrderValue: dto.totalOrderValue,
    totalRevenue: dto.totalRevenue,
    totalTips: dto.totalTips,
    totalPaidWithTips: dto.totalPaidWithTips,
    pendingAmount: dto.pendingAmount,
    collectionRate: dto.collectionRate,
    averageOrderValue: dto.averageOrderValue
  };
}


