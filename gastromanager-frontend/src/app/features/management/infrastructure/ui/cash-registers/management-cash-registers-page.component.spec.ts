import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementCashRegistersPageComponent } from './management-cash-registers-page.component';

describe('ManagementCashRegistersPageComponent', () => {
  let component: ManagementCashRegistersPageComponent;
  let fixture: ComponentFixture<ManagementCashRegistersPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagementCashRegistersPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagementCashRegistersPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display cash registers', () => {
    expect(component.cashRegisters.length).toBeGreaterThan(0);
  });

  it('should handle cash register status correctly', () => {
    expect(component.getCashRegisterStatusClass('active')).toBe('badge bg-success');
    expect(component.getCashRegisterStatusClass('closed')).toBe('badge bg-secondary');
    expect(component.getCashRegisterStatusClass('unknown')).toBe('badge bg-warning');
  });

  it('should return correct status text', () => {
    expect(component.getCashRegisterStatusText('active')).toBe('Activa');
    expect(component.getCashRegisterStatusText('closed')).toBe('Cerrada');
    expect(component.getCashRegisterStatusText('unknown')).toBe('Desconocido');
  });

  it('should call onOpenCashRegister with correct id', () => {
    spyOn(console, 'log');
    component.onOpenCashRegister(1);
    expect(console.log).toHaveBeenCalledWith('Abriendo caja registradora:', 1);
  });

  it('should call onCloseCashRegister with correct id', () => {
    spyOn(console, 'log');
    component.onCloseCashRegister(1);
    expect(console.log).toHaveBeenCalledWith('Cerrando caja registradora:', 1);
  });

  it('should call onViewCashRegisterDetails with correct id', () => {
    spyOn(console, 'log');
    component.onViewCashRegisterDetails(1);
    expect(console.log).toHaveBeenCalledWith('Ver detalles de caja registradora:', 1);
  });

  it('should call onViewTransactions with correct id', () => {
    spyOn(console, 'log');
    component.onViewTransactions(1);
    expect(console.log).toHaveBeenCalledWith('Ver transacciones de caja registradora:', 1);
  });
});
