import {Observable} from 'rxjs';
import {CashRegister} from '../models/cash-register.interface';

export abstract class CashRegistersRepository {
  abstract getAllCashRegisters(): Observable<CashRegister[]>;

  /*  abstract getCashRegisterByUuid(uuid: string): Observable<CashRegister>;
    abstract createCashRegister(cashRegister: CashRegisterCreateData): Observable<string>;
    abstract openCashRegister(openData: CashRegisterOpenData): Observable<string>;
    abstract closeCashRegister(closeData: CashRegisterCloseData): Observable<string>;
    abstract updateCashRegister(uuid: string, cashRegister: Partial<CashRegister>): Observable<CashRegister>;
    abstract deleteCashRegister(uuid: string): Observable<void>;*/
}
