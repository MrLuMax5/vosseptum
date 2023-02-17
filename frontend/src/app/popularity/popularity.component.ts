import { Component } from '@angular/core';
import {PopularityResponse} from "../../modules/vosssmolina-types";
import {CookieService} from "ngx-cookie-service";
import {PopularityService} from "./popularity-service.service";

@Component({
  selector: 'app-popularity',
  templateUrl: './popularity.component.html',
  styleUrls: ['./popularity.component.css']
})
export class PopularityComponent {

  popularityResponse!: PopularityResponse[];
  displayedColumns: string[] = ['email', 'avg_popularity', 'avg_budget'];

  constructor(private service: PopularityService, private cookieService: CookieService) {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.getMostImpactfulTopic(useMongo).subscribe( popularityResponse => {
      this.popularityResponse = popularityResponse;
    });
  }

}
