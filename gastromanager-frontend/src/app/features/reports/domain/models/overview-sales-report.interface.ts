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
  totalOrderValue: number;
  totalRevenue: number;
  totalTips: number;
  totalPaidWithTips: number;
  pendingAmount: number;
  collectionRate: number;
  averageOrderValue: number;
}


