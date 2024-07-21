import { Component } from '@angular/core';
import { RegistrationRequest } from '../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {}

  registerRequest: RegistrationRequest = {
    email: '',
    password: '',
    firstName: '',
    lastName: '',
  };

  errorMsg: Array<string> = [];
  register() {
    this.errorMsg = [];
    this.authService
      .register({
        body: this.registerRequest,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['activate-account']);
        },
        error: (err) => {
          console.log(err);
          this.errorMsg = err.error.validationErrors;
        },
      });
  }

  login() {
    this.router.navigate(['login']);
  }
}
