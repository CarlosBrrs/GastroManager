import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CashRegister } from '../../domain/models/cash-register.interface';
import { CashRegistersRepository } from '../../domain/ports/cash-registers.repository';
import { CashRegistersAdapter } from '../../infrastructure/api/cash-registers.adapter';

@Injectable({
  providedIn: 'root'
})
export class GetAllCashRegistersUseCase {
  private readonly cashRegistersRepo: CashRegistersRepository = inject(CashRegistersAdapter);

  getAllCashRegisters(): Observable<CashRegister[]> {
    return this.cashRegistersRepo.getAllCashRegisters();
  }
}
