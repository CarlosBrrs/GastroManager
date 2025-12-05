import {
  CashRegisterSession,
  PaymentMethodSummary,
  SessionSummary
} from '../../domain/models/cash-register-session.interface';
import {
  CashRegisterSessionResponseDto,
  PaymentMethodSummaryDto,
  SessionSummaryDataDto
} from '../../infrastructure/api/dtos/cash-register-session.dto';

// Mapper para CashRegisterSession - Actualizado para manejar nueva estructura
export function mapToCashRegisterSession(dto: CashRegisterSessionResponseDto): CashRegisterSession {
  return {
    uuid: dto.uuid,
    cashRegisterUuid: dto.cashRegisterUuid,
    cashRegisterName: dto.cashRegisterName,
    cashRegisterLocation: dto.cashRegisterLocation,
    initialAmount: dto.openingAmount || dto.initialAmount || 0,
    finalAmount: dto.closingAmount || dto.finalAmount,
    openedBy: dto.createdBy || dto.openedBy || 'unknown',
    closedBy: dto.closedBy,
    openedAt: dto.openingTime || dto.openedAt || dto.createdDate,
    closedAt: dto.closingTime || dto.closedAt,
    notes: dto.notes,
    status: dto.status as 'ACTIVE' | 'CLOSED'
  };
}

// Mapper para PaymentMethodSummary
function mapToPaymentMethodSummary(dto: PaymentMethodSummaryDto): PaymentMethodSummary {
  return {
    paymentMethodName: dto.paymentMethodName,
    paymentMethodDescription: dto.paymentMethodDescription,
    totalAmount: dto.totalAmount,
    transactionCount: dto.transactionCount
  };
}

// Mapper para SessionSummary
export function mapToSessionSummary(dto: SessionSummaryDataDto): SessionSummary {
  return {
    sessionUuid: dto.sessionUuid,
    cashRegisterUuid: dto.cashRegisterUuid,
    cashRegisterName: dto.cashRegisterName,
    cashRegisterLocation: dto.cashRegisterLocation,
    openedAt: dto.openedAt,
    openedBy: dto.openedBy,
    openingAmount: dto.openingAmount,
    totalCashPayments: dto.totalCashPayments,
    expectedCashAmount: dto.expectedCashAmount,
    totalCashMovements: dto.totalCashMovements,
    paymentMethodSummaries: dto.paymentMethodSummaries.map(mapToPaymentMethodSummary)
  };
}
