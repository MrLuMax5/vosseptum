import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Article, EditorialBoard, InterestedJournalist, Journalist, Topic} from "../../modules/vosssmolina-types";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PersonalSiteService {

  private readonly hostURL = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getConnections(id: number, useMongo: boolean): Observable<Journalist[]> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/journalist/connections");
    return this.http.post<Journalist[]>(url, id);
  }

  addConnection(id: number, email: string, useMongo: boolean): Observable<boolean> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/journalist/connections/add");
    const journalist: Journalist = {id: id, email: email};
    return this.http.post<boolean>(url, journalist);
  }

  getJournalistArticles(connections: Journalist[], useMongo: boolean): Observable<Article[]> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/article/related/byJournalists");
    return this.http.post<Article[]>(url, connections);
  }

  getTopicArticles(topics: Topic[], useMongo: boolean): Observable<Article[]> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/article/related/byTopics");
    return this.http.post<Article[]>(url, topics);
  }

  getInterestedTopics(id: number, useMongo: boolean) {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/topic/byJournalistId");
    return this.http.post<Topic[]>(url, id);
  }

  getTopicsOfConnections(connections: Journalist[], useMongo: boolean) {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/topic/byJournalistIds");
    return this.http.post<Topic[]>(url, connections);
  }

  addInterestedTopic(id: number, topic: string, useMongo: boolean): Observable<boolean> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo");
    }
    url = url.concat("/topic/addInterestedJournalist");
    const journalist: InterestedJournalist = {id: id, topic: topic};
    return this.http.post<boolean>(url, journalist);
  }

  getEditorialBoards(id: number, useMongo: boolean): Observable<EditorialBoard[]> {
    let url = this.hostURL;
    if (useMongo) {
      url = url.concat("/mongo/journalist");
    }
    url = url.concat("/editorial-board/byJournalistId");
    return this.http.post<EditorialBoard[]>(url, id);
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
