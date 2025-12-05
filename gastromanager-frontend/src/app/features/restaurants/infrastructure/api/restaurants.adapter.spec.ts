import {TestBed} from '@angular/core/testing';

import {RestaurantsAdapter} from './restaurants.adapter';

describe('RestaurantsService', () => {
  let service: RestaurantsAdapter;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RestaurantsAdapter);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
