import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PaymentsFormPageComponent} from './payments-form-page.component';

describe('PaymentsFormPageComponent', () => {
  let component: PaymentsFormPageComponent;
  let fixture: ComponentFixture<PaymentsFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaymentsFormPageComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PaymentsFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
