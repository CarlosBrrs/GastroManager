import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductItemFormComponent } from './product-item-form.component';

describe('ProductItemFormComponent', () => {
  let component: ProductItemFormComponent;
  let fixture: ComponentFixture<ProductItemFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductItemFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductItemFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
