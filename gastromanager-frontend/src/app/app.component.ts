import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {ConfirmationService, MessageService, PrimeNGConfig} from "primeng/api";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'gm-root',
  standalone: true,
  imports: [RouterOutlet, ToastModule],
  providers: [
    MessageService,
    ConfirmationService
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  constructor(private readonly primengConfig: PrimeNGConfig) {
  }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
  }
}
