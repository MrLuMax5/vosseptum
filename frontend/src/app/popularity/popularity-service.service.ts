import { Injectable } from '@angular/core';
import {PopularityResponse} from "../../modules/vosssmolina-types";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PopularityService {

  private readonly hostURL = `${window.location.origin}`;
  private readonly impactURL = `${this.hostURL}/data/mostImpactful`;

  constructor(private http: HttpClient) { }

  getMostImpactfulTopic(useMongo: boolean): Observable<PopularityResponse[]> {
    let url = this.impactURL;
    if (useMongo) {
      url = url.concat("/mongo")
    }
    return this.http.get<PopularityResponse[]>(url);
  }
}
