import {TestBed} from '@angular/core/testing';

import {IngredientsAdapter} from './ingredients.adapter';

describe('IngredientsService', () => {
  let service: IngredientsAdapter;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IngredientsAdapter);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
