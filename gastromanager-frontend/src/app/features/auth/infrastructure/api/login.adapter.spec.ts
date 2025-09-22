import { TestBed } from '@angular/core/testing';

import { LoginAdapter } from './login.adapter';

describe('LoginService', () => {
  let service: LoginAdapter;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginAdapter);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
