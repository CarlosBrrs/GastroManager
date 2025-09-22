import {Observable} from "rxjs";

export interface UserRepository {
  getUserByUsername(username: string): Observable<any>;
}
