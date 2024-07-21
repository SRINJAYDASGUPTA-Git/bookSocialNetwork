import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
redirectToLogin() {
this.router.navigate(['login']);
}
onCodeCompleted(token: string) {
  this.authService.confirm({
    token
  }).subscribe({
    next: () => {
      this.msg = "Account activated successfully!\n";
      this.submitted = true;
      this.isOkay = true;
    },
    error: (err) => {
      this.msg = "Token is invalid or expired.\n";
      this.submitted = true;
      this.isOkay = false;
    }
  });
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
