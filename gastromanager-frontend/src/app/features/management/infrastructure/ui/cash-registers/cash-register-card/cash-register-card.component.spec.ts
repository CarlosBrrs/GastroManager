import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CashRegisterCardComponent } from './cash-register-card.component';

describe('CashRegisterCardComponent', () => {
  let component: CashRegisterCardComponent;
  let fixture: ComponentFixture<CashRegisterCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CashRegisterCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CashRegisterCardComponent);
    component = fixture.componentInstance;

    // Mock data for testing
    component.cashRegister = {
      id: 1,
      name: 'Test Cash Register',
      status: 'active',
      location: 'Test Location',
      currentSession: {
        openedAt: '2025-03-09T08:00:00Z',
        openedBy: 'Test User',
        initialAmount: 50000
      }
    };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display cash register name', () => {
    expect(component.cashRegister.name).toBe('Test Cash Register');
  });

  it('should emit openCashRegister event', () => {
    spyOn(component.openCashRegister, 'emit');
    component.onOpenClick();
    expect(component.openCashRegister.emit).toHaveBeenCalledWith(1);
  });

  it('should emit closeCashRegister event', () => {
    spyOn(component.closeCashRegister, 'emit');
    component.onCloseClick();
    expect(component.closeCashRegister.emit).toHaveBeenCalledWith(1);
  });

  it('should emit viewDetails event', () => {
    spyOn(component.viewDetails, 'emit');
    component.onDetailsClick();
    expect(component.viewDetails.emit).toHaveBeenCalledWith(1);
  });

  it('should emit viewTransactions event', () => {
    spyOn(component.viewTransactions, 'emit');
    component.onTransactionsClick();
    expect(component.viewTransactions.emit).toHaveBeenCalledWith(1);
  });
});
