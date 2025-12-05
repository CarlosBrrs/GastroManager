import {TestBed} from '@angular/core/testing';

import {LoginUseCase} from './login.use-case';

describe('LoginUsecaseTsService', () => {
  let service: LoginUseCase;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginUseCase);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
