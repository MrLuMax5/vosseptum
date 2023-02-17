import { Injectable } from '@angular/core';
import {ArticleEditData} from "../../modules/vosssmolina-types";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  private readonly hostURL = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  sendArticle(article: ArticleEditData): Observable<boolean> {
    let url = this.hostURL;
    if (article.useMongo) {
      url = url.concat("/mongo")
    }
    url = url.concat("/article/post")
    return this.http.post<boolean>(url, article);
  }
}
