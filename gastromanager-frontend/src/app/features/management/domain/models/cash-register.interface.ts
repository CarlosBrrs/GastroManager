export interface CashRegister {
  uuid: string;
  name: string;
  location: string;
  status: 'ACTIVE' | 'CLOSED' | 'MAINTENANCE';
  currentSession?: CashRegisterSession;
  createdBy: string;
  createdDate: string;
  updatedBy?: string;
  updatedDate?: string;
}

export interface CashRegisterSession {
  uuid: string;
  openedAt: string;
  openedBy: string;
  initialAmount: number;
  closedAt?: string;
  closedBy?: string;
  finalAmount?: number;
  status: 'OPEN' | 'CLOSED';
}

export interface CashRegisterCreateData {
  name: string;
  location: string;
  initialAmount?: number;
}

export interface CashRegisterOpenData {
  cashRegisterUuid: string;
  initialAmount: number;
}

export interface CashRegisterCloseData {
  sessionUuid: string;
  finalAmount: number;
  notes?: string;
}
