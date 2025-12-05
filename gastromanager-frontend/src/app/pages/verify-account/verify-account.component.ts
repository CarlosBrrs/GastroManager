import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../core/services/auth/auth.service";
import {ProgressSpinnerModule} from "primeng/progressspinner";

@Component({
  selector: 'gm-verify-account',
  standalone: true,
  imports: [
    ProgressSpinnerModule
  ],
  templateUrl: './verify-account.component.html',
  styleUrl: './verify-account.component.scss'
})
export class VerifyAccountComponent implements OnInit {
  token: string | null = null;
  protected loading: boolean = true;

  constructor(private readonly route: ActivatedRoute, private readonly authService: AuthService, private readonly router: Router) {
  }

  ngOnInit(): void {
    // Obtiene el token del query parameter, por ejemplo, /verify?token=abc123
    this.token = this.route.snapshot.queryParamMap.get('token');
    console.log('Token recibido:', this.token);
    // Llama al servicio para verificar el token
    if (this.token) {
      this.authService.verifyAccount(this.token).subscribe({
        next: () => {
          this.loading = false;
          console.log('Su cuenta ha sido verificada con éxito. Redirigiendo al login...');
          setTimeout(() => this.router.navigate(['/login']), 3000);
        },
        error: (error) => {
          this.loading = false;
          console.log(error.message || 'Ocurrió un error durante la verificación. Intente nuevamente o revise su correo para un nuevo token.');
        }
      });
    } else {
      this.loading = false;
      console.log('Token no encontrado. Por favor, use el enlace proporcionado en su correo.');
    }
  }

  redirectToLogin(): void {
    this.router.navigate(['/login']);
  }

}
