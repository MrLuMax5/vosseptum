import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Journalist} from "../../modules/vosssmolina-types";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly hostURL = `${window.location.origin}`;
  private readonly loginURL = `${this.hostURL}/login`;

  constructor(private http: HttpClient,
              private router: Router,
              private cookieService: CookieService,
              private snackBar: MatSnackBar) { }

  isAuthenticated(): boolean {
    // Is cookie set?
    return !!this.cookieService.get("login");
  }

  authenticate(journalist: Journalist): void {
    this.http.post<number>(this.loginURL, journalist).subscribe({
      next: (data) => {
        if (data) {
          this.snackBar.dismiss();
          this.cookieService.set("login", data.toString());
          this.router.navigateByUrl("/personal-site");
        }
      },
      error: err => {
        this.snackBar.open("Username or password invalid!", "Ok");
      }
    })
  }

  signOut(): void {
    // Remove Cookie
    this.cookieService.delete("login");
    this.router.navigateByUrl("/login");
  }
}
