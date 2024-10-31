import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductItemTableComponent } from './product-item-table.component';

describe('ProductItemTableComponent', () => {
  let component: ProductItemTableComponent;
  let fixture: ComponentFixture<ProductItemTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductItemTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductItemTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
