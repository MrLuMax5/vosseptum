import { Component } from '@angular/core';
import {TrustworthyService} from "./trustworthy.service";
import {TrustResponse} from "../../modules/vosssmolina-types";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-trustworthy',
  templateUrl: './trustworthy.component.html',
  styleUrls: ['./trustworthy.component.css']
})
export class TrustworthyComponent {

  trustResponse!: TrustResponse[];
  displayedColumns: string[] = ['avgGrade', 'email'];

  constructor(private service: TrustworthyService, private cookieService: CookieService) {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.getMostTrustworthyJournalists(useMongo).subscribe( trustResponse => {
      this.trustResponse = trustResponse;
    });
  }

}
