import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {TrustResponse} from "../../modules/vosssmolina-types";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TrustworthyService {

  private readonly hostURL = `${window.location.origin}`;
  private readonly trustURL = `${this.hostURL}/data/mostTrustworthies`;

  constructor(private http: HttpClient) { }

  getMostTrustworthyJournalists(useMongo: boolean): Observable<TrustResponse[]> {
    let url = this.trustURL;
    if (useMongo) {
      url = url.concat("/mongo")
    }
    return this.http.get<TrustResponse[]>(url);
  }
}
