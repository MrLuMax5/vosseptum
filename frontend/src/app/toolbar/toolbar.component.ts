import {Component, Input} from '@angular/core';
import {AuthService} from "../shared/auth-service.service";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent {

  @Input() title!: string;
  useMongo!: boolean;

  constructor(private authService: AuthService,
              private cookieService: CookieService) {
    if (!this.cookieService.get("useMongo")){
      this.cookieService.set("useMongo", "0");
    }
    this.useMongo = this.cookieService.get("useMongo") === "1";
  }

  logout() {
    this.authService.signOut();
  }

  isLoggedIn(): boolean {
    return this.authService.isAuthenticated();
  }

  changeDatabase() {
    if (this.useMongo) {
      this.cookieService.set("useMongo", "1");
    } else {
      this.cookieService.set("useMongo", "0");
    }
  }
}
