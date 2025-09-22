// Interfaces del dominio para Cash Register Sessions

export interface CashRegisterSession {
  uuid: string;
  cashRegisterUuid: string;
  cashRegisterName?: string;
  cashRegisterLocation?: string;
  initialAmount: number;
  finalAmount?: number;
  openedBy: string;
  closedBy?: string;
  openedAt: string;
  closedAt?: string;
  notes?: string;
  status: 'ACTIVE' | 'CLOSED';
}

export interface OpenSessionData {
  cashRegisterUuid: string;
  initialAmount: number;
  notes?: string;
}

export interface CloseSessionData {
  sessionUuid: string;
  finalAmount: number;
  notes?: string;
}

// Nuevas interfaces para Session Summary (dominio)
export interface PaymentMethodSummary {
  paymentMethodName: string;
  paymentMethodDescription: string;
  totalAmount: number;
  transactionCount: number;
}

export interface SessionSummary {
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
  paymentMethodSummaries: PaymentMethodSummary[];
}
