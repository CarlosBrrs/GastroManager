import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientsPageComponent } from './ingredients-page.component';

describe('IngredientsComponent', () => {
  let component: IngredientsPageComponent;
  let fixture: ComponentFixture<IngredientsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngredientsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IngredientsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
