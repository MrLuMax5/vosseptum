import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {FillResponse} from "../../modules/vosssmolina-types";

@Injectable({
  providedIn: 'root'
})
export class MainpageService {

  private readonly hostURL = `${window.location.origin}`;
  private readonly fillURL = `${this.hostURL}/data/fill`;
  private readonly countURL = `${this.hostURL}/data/count`;
  private readonly migrationURL = `${this.hostURL}/migrate`;

  constructor(private http: HttpClient) {
  }

  fillDataCall(): Observable<FillResponse> {
    return this.http.get<FillResponse>(this.fillURL);
  }

  getEntityAndBridgeCount(): Observable<FillResponse> {
    return this.http.get<FillResponse>(this.countURL);
  }

  migrateData(): Observable<boolean> {
    return this.http.get<boolean>(this.migrationURL);
  }
}
