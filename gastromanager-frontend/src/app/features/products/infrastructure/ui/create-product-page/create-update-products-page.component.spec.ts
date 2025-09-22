import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateProductsPageComponent } from './create-update-products-page.component';

describe('CreateProductPageComponent', () => {
  let component: CreateUpdateProductsPageComponent;
  let fixture: ComponentFixture<CreateUpdateProductsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateProductsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateProductsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
