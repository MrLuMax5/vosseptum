import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ArticleKey, Journalist, TruthfulnessReport, TruthfulnessReportData} from "../../modules/vosssmolina-types";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private readonly hostURL = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  getReportsForArticleURL(articleKey: ArticleKey, useMongo: boolean): Observable<TruthfulnessReport[]> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/truthfulness-report/byArticleId");
    return this.http.post<TruthfulnessReport[]>(url, articleKey);
  }

  sendReport(reportData: TruthfulnessReportData, useMongo: boolean): Observable<boolean> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/truthfulness-report/add");
    return this.http.post<boolean>(url, reportData.report);
  }

  getConnections(id: number, useMongo: boolean): Observable<Journalist[]> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/journalist/connections");
    return this.http.post<Journalist[]>(url, id);
  }

  getJournalistById(id: number, useMongo: boolean): Observable<Journalist> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/journalist");
    return this.http.post<Journalist>(url, id);
  }
}
