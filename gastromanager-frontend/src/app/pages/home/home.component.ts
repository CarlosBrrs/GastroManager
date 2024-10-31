import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserResponseDto} from "../../core/model/interfaces/UserResponseDto";
import {UserService} from "../../core/services/users/user.service";
import {Subject, takeUntil} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'gm-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, OnDestroy {

  userInfo!: UserResponseDto;
  private destroy$ = new Subject();
  private hasRedirected = false;

  constructor(private router: Router, private userService: UserService) {

  }


  ngOnInit(): void {
// TODO: Dont call the endpoint always, instead save the user info in the service and call the signal
    this.userService.loadUserInfo().pipe(takeUntil(this.destroy$)).subscribe({
      next: (result) => {
        this.userInfo = result.data;
      },
      error: (response) => {
        if (!this.hasRedirected && response.status === 401) { // 401 para token no autorizado
          this.hasRedirected = true; // evita múltiples redirecciones
          this.router.navigate(['login']);
        }
      },
      complete: () => {
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next(this.userService.loadUserInfo());  // trigger the unsubscribe
    this.destroy$.complete();
  }

}
