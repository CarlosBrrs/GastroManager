import {Component, computed, effect, inject} from '@angular/core';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthFacade} from "../../../application/facades/auth.facade";
import {Login} from "../../../domain/models/login.model";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {ButtonModule} from "primeng/button";
import {Router} from "@angular/router";
import {AuthStore} from "../../../../../core/store/auth/auth.store";

@Component({
  selector: 'gm-login-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ProgressSpinnerModule,
    InputTextModule,
    PasswordModule,
    ButtonModule
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly authStore = inject(AuthStore);
  private readonly router = inject(Router);

  readonly loginForm = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  readonly loading = computed(() => this.authStore.loading());
  readonly error = computed(() => this.authStore.error());

  constructor() {
    effect(() => {
      if (this.authStore.isAuthenticated()) {
        this.router.navigate(['/dashboard']);
      }
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    const credentials: Login = this.loginForm.value as Login;
    this.authStore.login(credentials);
  }
}
