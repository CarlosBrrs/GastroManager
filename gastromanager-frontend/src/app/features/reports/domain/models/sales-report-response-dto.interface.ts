export interface SalesReportResponseDto {
  totalSales: number;
  totalOrders: number;
  reportPeriodStart: string;
  reportPeriodEnd: string;
  summary: SalesReportSummaryDto;
  details: SalesReportDetailDto[];
}

export interface SalesReportSummaryDto {
  totalRevenue: number;
  totalTips: number;
  completedOrders: number;
  cancelledOrders: number;
  averageOrderValue: number;
  paymentMethodBreakdown: PaymentMethodSummaryDto[];
  cashRegisterBreakdown: CashRegisterSummaryDto[];
}

export interface PaymentMethodSummaryDto {
  methodName: string;
  totalAmount: number;
  transactionCount: number;
  percentage: number;
}

export interface CashRegisterSummaryDto {
  cashRegisterUuid: string;
  cashRegisterName: string;
  totalSales: number;
  orderCount: number;
  transactionCount: number;
  sessionSummaries: CashRegisterSessionSummaryDto[];
}

export interface CashRegisterSessionSummaryDto {
  sessionUuid: string;
  operatorUserUuid: string;
  operatorUserName: string;
  sessionOpenTime: string;
  sessionCloseTime: string | null;
  sessionStatus: string;
  sessionSales: number;
  sessionOrderCount: number;
  sessionTransactionCount: number;
}

export interface SalesReportDetailDto {
  paymentMethodName: string;
  totalAmount: number;
  orderCount: number;
  transactionCount: number;
  averageTransactionAmount: number;
}

export interface SalesReportFiltersDto {
  dateFrom: string;
  dateTo: string;
  cashRegisterUuids?: string[];
  sessionState?: 'ALL' | 'OPEN' | 'CLOSED';
  paymentMethods?: string[];
  assignedUserUuids?: string[];
}
