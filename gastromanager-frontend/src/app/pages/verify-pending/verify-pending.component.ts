import { Component } from '@angular/core';

@Component({
  selector: 'gm-verify-pending',
  standalone: true,
  imports: [],
  templateUrl: './verify-pending.component.html',
  styleUrl: './verify-pending.component.scss'
})
export class VerifyPendingComponent {

  resendVerification() {
    window.alert("to resend a verification code upon request")
  }
}
