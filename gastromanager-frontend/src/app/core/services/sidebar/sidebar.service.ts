import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {UserService} from "../users/user.service";

@Injectable({
  providedIn: 'root'
})
export class SidebarService {

  userInfo: any;

  constructor(private authService: AuthService, private userService: UserService) {
    this.userInfo = this.userService.userInfo()
  }


  getMenuItems() {

  }
}
