import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Journalist} from "../../modules/vosssmolina-types";

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  private readonly hostURL = `${window.location.origin}`;

  constructor(private http: HttpClient) { }

  fillDataCall(journalist: Journalist, useMongo: boolean): Observable<Journalist> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/journalist/add");
    return this.http.post<Journalist>(url, journalist);
  }
}
