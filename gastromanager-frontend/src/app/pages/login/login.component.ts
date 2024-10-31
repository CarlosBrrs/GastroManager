import {Component, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../core/services/auth/auth.service";

@Component({
  selector: 'gm-login',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService) {
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
        error: (response) => {
          console.error("Error al hacer login: ", response);
          alert(response.error.message);
        },
        complete: () => {
          // this.isLoading = false;
        }
      }
    )
  }

}
