import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyMetricsComponent } from './key-metrics.component';

describe('KeyMetricsComponent', () => {
  let component: KeyMetricsComponent;
  let fixture: ComponentFixture<KeyMetricsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KeyMetricsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KeyMetricsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
