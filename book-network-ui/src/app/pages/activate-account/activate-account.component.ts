import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
onCodeCompleted($event: string) {
throw new Error('Method not implemented.');
}
onCodeChanged($event: string) {
throw new Error('Method not implemented.');
}
  msg: string = "";
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  activateAccount() {
  }
}
