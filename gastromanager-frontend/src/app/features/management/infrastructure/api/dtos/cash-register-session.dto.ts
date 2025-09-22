// DTOs para las respuestas del backend de sesiones
export interface CashRegisterSessionResponseDto {
  uuid: string;
  cashRegisterUuid: string;
  cashRegisterName?: string;
  cashRegisterLocation?: string;
  openingTime?: string;
  closingTime?: string;
  openingAmount: number;
  closingAmount?: number;
  status: string;
  createdBy: string;
  createdDate: string;
  // Campos legacy para compatibilidad
  initialAmount?: number;
  finalAmount?: number;
  openedBy?: string;
  closedBy?: string;
  openedAt?: string;
  closedAt?: string;
  notes?: string;
}

// DTO para el request de abrir sesión
export interface OpenSessionRequestDto {
  cashRegisterUuid: string;
  openingAmount: number;
  notes?: string;
}

// DTO para el request de cerrar sesión
export interface CloseSessionRequestDto {
  closingAmount: number;
  notes?: string;
}

// Nuevas interfaces para Session Summary
export interface PaymentMethodSummaryDto {
  paymentMethodName: string;
  paymentMethodDescription: string;
  totalAmount: number;
  transactionCount: number;
}

export interface SessionSummaryDataDto {
  sessionUuid: string;
  cashRegisterUuid: string;
  cashRegisterName: string;
  cashRegisterLocation: string;
  openedAt: string;
  openedBy: string;
  openingAmount: number;
  totalCashPayments: number;
  expectedCashAmount: number;
  totalCashMovements: number;
  paymentMethodSummaries: PaymentMethodSummaryDto[];
}

export interface SessionSummaryResponseDto {
  timestamp: string;
  flag: boolean;
  message: string;
  data: SessionSummaryDataDto;
}
