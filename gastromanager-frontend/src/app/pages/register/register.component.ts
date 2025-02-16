import {Component, OnInit} from '@angular/core';
import {RegisterFormComponent} from "./register-form/register-form.component";
import {AuthService} from "../../core/services/auth/auth.service";
import {Router} from "@angular/router";
import {StoreEventService} from "../../core/services/store-event/store-event.service";
import {MessageService} from "primeng/api";

export interface SignupRequestDto {
  name: string;
  lastname: string;
  username: string;
  password: string;
  contact: ContactRequestDto;
  subscription: SubscriptionRequestDto;
}

interface ContactRequestDto {
  email: string;
  phone: string;
  website: string;
}
interface SubscriptionRequestDto {
  plan: string;
  paymentToken: string; // O Date si vas a manejar objetos de fecha
}

@Component({
  selector: 'gm-register',
  standalone: true,
  imports: [
    RegisterFormComponent
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit {

  loading: boolean = false;

  constructor(private readonly authService: AuthService, private readonly router: Router, private readonly messageService: MessageService) {

  }
  ngOnInit(): void {

  }

  handleFormSubmit(signup: SignupRequestDto) {
    this.loading = true;
    this.authService.signup(signup).subscribe({
      next: (response) => {
        this.loading = false;
        // Redirigir a la página de verificación
        this.router.navigate(['/verify-pending']).then(r => console.log("account verified, go to email", r));
      },
      error: (error) => {
        this.loading = false;
        this.messageService.add({severity: 'error', summary: 'Error on signup', detail: error.message});
        // Aquí podrías mostrar un mensaje de error, pero el formulario conserva los datos ingresados
        console.error("Error en el registro:", error.message);
      }
    });
  }
}
