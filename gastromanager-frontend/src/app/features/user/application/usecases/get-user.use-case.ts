import {Inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {UserRepository} from "../../domain/ports/user.repository";
import {UserAdapter} from "../../infrastructure/api/user.adapter";
import {User} from "../../../../core/store/auth/auth.store";

@Injectable({
  providedIn: 'root'
})
export class GetUserUseCase {

  constructor(@Inject(UserAdapter) private readonly userRepo: UserRepository) {
  }

  execute(uuid: string): Observable<User> {
    return this.userRepo.getUserByUsername(uuid);
  }
}
