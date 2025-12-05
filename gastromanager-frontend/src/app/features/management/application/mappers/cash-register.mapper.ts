import {CashRegister, CashRegisterSession} from '../../domain/models/cash-register.interface';
import {
  CashRegisterCurrentSessionResponseDto,
  CashRegisterResponseDto
} from '../../infrastructure/api/cash-registers.adapter';

export function mapToCashRegister(dto: CashRegisterResponseDto): CashRegister {
  return {
    uuid: dto.uuid,
    name: dto.name,
    location: dto.location,
    status: mapStatus(dto.status),
    currentSession: dto.currentSession ? mapToCurrentSession(dto.currentSession) : undefined,
    createdBy: '', // No viene en la respuesta actual
    createdDate: '', // No viene en la respuesta actual
    updatedBy: undefined,
    updatedDate: undefined
  };
}

function mapToCurrentSession(sessionDto: CashRegisterCurrentSessionResponseDto): CashRegisterSession {
  return {
    uuid: sessionDto.uuid,
    openedAt: sessionDto.openedAt,
    openedBy: sessionDto.openedBy,
    initialAmount: sessionDto.openingAmount,
    closedAt: undefined,
    closedBy: undefined,
    finalAmount: undefined,
    status: 'OPEN' // Si existe currentSession, está abierta
  };
}

function mapStatus(backendStatus: string): 'ACTIVE' | 'CLOSED' | 'MAINTENANCE' {
  switch (backendStatus) {
    case 'OPEN':
      return 'ACTIVE';
    case 'CLOSED':
      return 'CLOSED';
    default:
      return 'MAINTENANCE';
  }
}
