import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateMenusPageComponent } from './create-update-menus-page.component';

describe('CreateUpdateMenusPageComponent', () => {
  let component: CreateUpdateMenusPageComponent;
  let fixture: ComponentFixture<CreateUpdateMenusPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateMenusPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateMenusPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
