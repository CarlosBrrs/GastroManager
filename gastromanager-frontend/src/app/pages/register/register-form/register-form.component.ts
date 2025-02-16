import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {SubscriptionPlanStore} from "../../../core/store/plan/plan.store";
import {SpinnerModule} from "primeng/spinner";
import {StyleClassModule} from "primeng/styleclass";
import {ProgressSpinnerModule} from "primeng/progressspinner";

@Component({
  selector: 'gm-register-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    InputTextModule,
    PasswordModule,
    SpinnerModule,
    StyleClassModule,
    ProgressSpinnerModule
  ],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.scss'
})
export class RegisterFormComponent implements OnInit {
  registrationForm!: FormGroup;
  subscriptionPlanStore = inject(SubscriptionPlanStore);
  @Output() formSubmitted = new EventEmitter<any>();
  @Input() loading: boolean = false;
  constructor(private readonly fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.registrationForm = this.fb.group({
      name: ['', Validators.required],
      lastname: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      contact: this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        website: [''],
        phone: ['']
      }),
      subscription: this.fb.group({
        plan: ['', [Validators.required]],
        paymentToken: ['', [Validators.required]],
      })
    });
  }

  onSubmit() {

    if (this.registrationForm.valid) {

      this.formSubmitted.emit(this.registrationForm.value)
    }
  }

  simulatePayment() {

  }
}
