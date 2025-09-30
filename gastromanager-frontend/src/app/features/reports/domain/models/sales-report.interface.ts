export interface SalesReport {
  totalSales: number;
  totalOrders: number;
  reportPeriodStart: Date;
  reportPeriodEnd: Date;
  summary: SalesReportSummary;
  details: SalesReportDetail[];
}

export interface SalesReportSummary {
  totalRevenue: number;
  totalTips: number;
  completedOrders: number;
  cancelledOrders: number;
  averageOrderValue: number;
  paymentMethodBreakdown: PaymentMethodSummary[];
  cashRegisterBreakdown: CashRegisterSummary[];
}

export interface PaymentMethodSummary {
  methodName: string;
  totalAmount: number;
  transactionCount: number;
  percentage: number;
}

export interface CashRegisterSummary {
  cashRegisterUuid: string;
  cashRegisterName: string;
  totalSales: number;
  orderCount: number;
  transactionCount: number;
  sessionSummaries: CashRegisterSessionSummary[];
}

export interface CashRegisterSessionSummary {
  sessionUuid: string;
  operatorUserUuid: string;
  operatorUserName: string;
  sessionOpenTime: Date;
  sessionCloseTime: Date | null;
  sessionStatus: string;
  sessionSales: number;
  sessionOrderCount: number;
  sessionTransactionCount: number;
}

export interface SalesReportDetail {
  paymentMethodName: string;
  totalAmount: number;
  orderCount: number;
  transactionCount: number;
  averageTransactionAmount: number;
}

export interface SalesReportFilters {
  dateFrom: Date;
  dateTo: Date;
  cashRegisterUuids?: string[];
  sessionState?: 'ALL' | 'OPEN' | 'CLOSED';
  paymentMethods?: string[];
  assignedUserUuids?: string[];
}
