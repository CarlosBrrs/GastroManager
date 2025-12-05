import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../core/services/auth/auth.service";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {Ripple} from "primeng/ripple";
import {ButtonDirective} from "primeng/button";
import {CheckboxModule} from "primeng/checkbox";

@Component({
  selector: 'gm-login',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ToastModule,
    InputTextModule,
    PasswordModule,
    Ripple,
    ButtonDirective,
    CheckboxModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(private readonly fb: FormBuilder, private readonly router: Router, private readonly authService: AuthService, private messageService: MessageService) {
    this.loginForm = this.fb.group({
      username: new FormControl<string>("", [Validators.required]),
      password: new FormControl<string>("", [Validators.required, Validators.minLength(4)]),
    })
  }

  ngOnInit(): void {
    // Verificar si ya está autenticado
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['home']); // Redireccionar si ya está autenticado
    }
  }

  handleLoginSubmit() {
    this.authService.login(this.loginForm.value).subscribe({
        next: (result) => {
          this.router.navigate(['home']).then(success => {
            if (success) {
              console.log("Navegación exitosa a home");
            } else {
              console.log("La navegación a home falló");
            }
          });
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error on login',
            detail: error.error.message || error.message
          });
          console.error("Error al hacer login: ", error);
        },
        complete: () => {
          // this.isLoading = false;
        }
      }
    )
  }

}
