export interface OverviewSalesReport {
  filters: SalesReportFiltersInfo;
  summary: OverviewSalesReportSummary;
}

export interface SalesReportFiltersInfo {
  startDate: Date;
  endDate: Date;
  totalDays: number;
}

export interface OverviewSalesReportSummary {
  totalOrders: number;
  completedOrders: number;
  cancelledOrders: number;
  cancellationRate: number;
  totalRevenue: number;
  totalPaidWithTips: number;
  averageOrderValue: number;
}


