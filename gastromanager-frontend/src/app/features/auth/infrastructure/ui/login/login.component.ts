import {Component, computed, effect, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthFacade} from "../../../application/facades/auth.facade";
import {Login} from "../../../domain/models/login.model";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {Router} from "@angular/router";
import {AuthStore} from "../../../../../core/store/auth/auth.store";

@Component({
  selector: 'gm-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  store = inject(AuthStore)
  loading = computed(() => this.store.loading());
  error = computed(() => this.store.error());

  constructor(private readonly fb: FormBuilder, private readonly authFacade: AuthFacade, private readonly router: Router) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    effect(() => {
      if (this.store.isAuthenticated()) {
        this.router.navigate(['/dashboard']);
      }
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) return;
    const credentials: Login = this.loginForm.value;
    this.store.login(credentials);
  }
}
