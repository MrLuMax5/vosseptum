import { Component } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {RegistrationService} from "./registration.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  form: FormGroup = new FormGroup<any>({
    email: new FormControl(''),
    password: new FormControl(''),
    location: new FormControl(''),
  });

  constructor(private service: RegistrationService,
              private snackBar: MatSnackBar,
              private router: Router,
              private cookieService: CookieService) {
  }

  register(): void {
    if (this.form.valid) {
      const useMongo = this.cookieService.get("useMongo") === "1";
      this.service.fillDataCall(this.form.value, useMongo) // {email: 'xyz', password: 'xyz', location: 'xyz'}
        .subscribe({
          next: (journalist) => {
            console.log(journalist);
            this.router.navigateByUrl("/");
            this.openSnackBar();
          }
        });
      this.form.reset();
    }
  }

  openSnackBar(): void {
    this.snackBar.open("Registration successfull!", "Ok");
  }
}
