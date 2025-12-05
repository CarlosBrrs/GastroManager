import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateUpdateSubmenusPageComponent} from './create-update-submenus-page.component';

describe('CreateUpdateMenusPageComponent', () => {
  let component: CreateUpdateSubmenusPageComponent;
  let fixture: ComponentFixture<CreateUpdateSubmenusPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateSubmenusPageComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateSubmenusPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
