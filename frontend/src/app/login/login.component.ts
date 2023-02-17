import { Component } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {AuthService} from "../shared/auth-service.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  form: FormGroup = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
  });

  constructor(private authService: AuthService, private snackBar: MatSnackBar) {
  }

  submit() {
    if (this.form.valid) {
      this.snackBar.dismiss();
      this.authService.authenticate(this.form.value);
    }
    else {
      this.snackBar.open("Username or password invalid", "Ok");
    }
  }

}
