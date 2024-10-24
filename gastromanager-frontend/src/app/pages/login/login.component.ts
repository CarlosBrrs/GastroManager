import {Component, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {LayoutComponent} from "../../core/layout/layout.component";
import {AuthService} from "../../core/services/auth.service";

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
    this.authService.isLoggedIn.set(false);
    this.loginForm = this.fb.group({
      username: new FormControl<string>("", [Validators.required]),
      password: new FormControl<string>("", [Validators.required, Validators.minLength(4)]),
    })
  }

  ngOnInit(): void {
    // Verificar si ya está autenticado
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/home']); // Redireccionar si ya está autenticado
    }
  }

  handleLoginSubmit() {
    console.log(this.loginForm.value);

    this.authService.login(this.loginForm.value).subscribe({
      next: (result) => {
        // const data: LoginResponseDto = result.data as LoginResponseDto;

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
