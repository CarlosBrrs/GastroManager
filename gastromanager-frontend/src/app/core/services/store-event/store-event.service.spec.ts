import { TestBed } from '@angular/core/testing';

import { StoreEventService } from './store-event.service';

describe('StoreEventService', () => {
  let service: StoreEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StoreEventService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
