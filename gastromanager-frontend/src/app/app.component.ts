import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {ConfirmationService, MessageService, PrimeNGConfig} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {environment} from "../environments/environment";

@Component({
  selector: 'gm-root',
  standalone: true,
  imports: [RouterOutlet, LoginComponent, ToastModule],
  providers: [
    MessageService,
    ConfirmationService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  constructor(private primengConfig: PrimeNGConfig) {
  }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
    console.log("Envornment loadad " , environment)
  }
}
