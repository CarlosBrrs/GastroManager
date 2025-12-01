export interface OverviewSalesReportResponseDto {
  filters: SalesReportFiltersInfoDto;
  summary: SalesReportSummaryDto;
}

export interface SalesReportFiltersInfoDto {
  startDate: string;
  endDate: string;
  totalDays: number;
}

export interface SalesReportSummaryDto {
  totalOrders: number;
  completedOrders: number;
  cancelledOrders: number;
  cancellationRate: number;
  totalRevenue: number;
  totalPaidWithTips: number;
  averageOrderValue: number;
}

