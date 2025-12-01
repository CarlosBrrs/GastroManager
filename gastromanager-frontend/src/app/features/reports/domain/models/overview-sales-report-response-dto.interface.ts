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
  totalOrderValue: number;
  totalRevenue: number;
  totalTips: number;
  totalPaidWithTips: number;
  pendingAmount: number;
  collectionRate: number;
  averageOrderValue: number;
}

