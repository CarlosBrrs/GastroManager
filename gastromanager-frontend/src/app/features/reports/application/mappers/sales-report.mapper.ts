import {
  CashRegisterSessionSummary,
  CashRegisterSummary,
  PaymentMethodSummary,
  SalesReport,
  SalesReportDetail,
  SalesReportFilters,
  SalesReportSummary
} from "../../domain/models/sales-report.interface";
import {
  CashRegisterSessionSummaryDto,
  CashRegisterSummaryDto,
  PaymentMethodSummaryDto,
  SalesReportDetailDto,
  SalesReportFiltersDto,
  SalesReportResponseDto,
  SalesReportSummaryDto
} from "../../domain/models/sales-report-response-dto.interface";

export function mapToSalesReport(dto: SalesReportResponseDto): SalesReport {
  return {
    totalSales: dto.totalSales,
    totalOrders: dto.totalOrders,
    reportPeriodStart: new Date(dto.reportPeriodStart),
    reportPeriodEnd: new Date(dto.reportPeriodEnd),
    summary: mapToSalesReportSummary(dto.summary),
    details: dto.details.map(detail => mapToSalesReportDetail(detail))
  };
}

export function mapToSalesReportFiltersDto(filters: SalesReportFilters): SalesReportFiltersDto {
  return {
    dateFrom: filters.dateFrom.toISOString(), // Envía en formato ISO UTC
    dateTo: filters.dateTo.toISOString(),     // Envía en formato ISO UTC
    cashRegisterUuids: filters.cashRegisterUuids,
    sessionState: filters.sessionState,
    paymentMethods: filters.paymentMethods,
    assignedUserUuids: filters.assignedUserUuids
  };
}

function mapToSalesReportSummary(dto: SalesReportSummaryDto): SalesReportSummary {
  return {
    totalRevenue: dto.totalRevenue,
    totalTips: dto.totalTips,
    completedOrders: dto.completedOrders,
    cancelledOrders: dto.cancelledOrders,
    averageOrderValue: dto.averageOrderValue,
    paymentMethodBreakdown: dto.paymentMethodBreakdown.map(pm => mapToPaymentMethodSummary(pm)),
    cashRegisterBreakdown: dto.cashRegisterBreakdown.map(cr => mapToCashRegisterSummary(cr))
  };
}

function mapToSalesReportDetail(dto: SalesReportDetailDto): SalesReportDetail {
  return {
    paymentMethodName: dto.paymentMethodName,
    totalAmount: dto.totalAmount,
    orderCount: dto.orderCount,
    transactionCount: dto.transactionCount,
    averageTransactionAmount: dto.averageTransactionAmount
  };
}

function mapToPaymentMethodSummary(dto: PaymentMethodSummaryDto): PaymentMethodSummary {
  return {
    methodName: dto.methodName,
    totalAmount: dto.totalAmount,
    transactionCount: dto.transactionCount,
    percentage: dto.percentage
  };
}

function mapToCashRegisterSummary(dto: CashRegisterSummaryDto): CashRegisterSummary {
  return {
    cashRegisterUuid: dto.cashRegisterUuid,
    cashRegisterName: dto.cashRegisterName,
    totalSales: dto.totalSales,
    orderCount: dto.orderCount,
    transactionCount: dto.transactionCount,
    sessionSummaries: dto.sessionSummaries.map(session => mapToCashRegisterSessionSummary(session))
  };
}

function mapToCashRegisterSessionSummary(dto: CashRegisterSessionSummaryDto): CashRegisterSessionSummary {
  return {
    sessionUuid: dto.sessionUuid,
    operatorUserUuid: dto.operatorUserUuid,
    operatorUserName: dto.operatorUserName,
    sessionOpenTime: new Date(dto.sessionOpenTime),
    sessionCloseTime: dto.sessionCloseTime ? new Date(dto.sessionCloseTime) : null,
    sessionStatus: dto.sessionStatus,
    sessionSales: dto.sessionSales,
    sessionOrderCount: dto.sessionOrderCount,
    sessionTransactionCount: dto.sessionTransactionCount
  };
}
