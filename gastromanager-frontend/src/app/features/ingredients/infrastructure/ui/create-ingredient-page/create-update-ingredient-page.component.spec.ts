import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateIngredientPageComponent } from './create-update-ingredient-page.component';

describe('CreateIngredientPageComponent', () => {
  let component: CreateUpdateIngredientPageComponent;
  let fixture: ComponentFixture<CreateUpdateIngredientPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateIngredientPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateIngredientPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
