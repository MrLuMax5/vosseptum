import {Component} from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {Article, EditorialBoard, Journalist, Topic} from "../../modules/vosssmolina-types";
import {PersonalSiteService} from "./personal-site.service";
import {MatDialog} from "@angular/material/dialog";
import {ConnectionDialogComponent} from "./connection-dialog/connection-dialog.component";
import {TopicDialogComponent} from "./topic-dialog/topic-dialog.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ArticleEditModalComponent} from "../article-edit-modal/article-edit-modal.component";
import {ArticleService} from "../shared/article-service.service";
import {combineLatestWith} from "rxjs";

@Component({
  selector: 'personal-site',
  templateUrl: './personal-site.component.html',
  styleUrls: ['./personal-site.component.css']
})
export class PersonalSiteComponent {

  id: number;
  connections: Journalist[] = [];
  articles: Article[] = [];
  topics: Topic[] = [];
  boards: EditorialBoard[] = [];
  byPersonal = true;
  byConnections = false;
  byTopics = false;

  constructor(private cookieService: CookieService,
              private service: PersonalSiteService,
              private articleService: ArticleService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
    this.id = Number(cookieService.get("login"));
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.getConnections(this.id, useMongo).subscribe(data => {
      this.connections = data;
    });
    this.service.getJournalistArticles([{id: this.id}], useMongo).subscribe(data => {
      this.articles = data;
    });
    this.service.getInterestedTopics(this.id, useMongo).subscribe(data => {
      this.topics = data;
    });
    this.service.getEditorialBoards(this.id, useMongo).subscribe(data => {
      this.boards = data;
    })
  }

  openConnectionDialog(): void {
    const dialogRef = this.dialog.open(ConnectionDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.sendConnectionRequest(result);
      }
    });
  }

  openTopicDialog(): void {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.getTopicsOfConnections(this.connections, useMongo).subscribe(data => {
      data = data.filter(topic => this.topics.indexOf(topic) === -1);
      const dialogRef = this.dialog.open(TopicDialogComponent, {
        data: data.slice(0, 5),
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.sendTopicInterestRequest(result);
        }
      });
    });
  }

  sendConnectionRequest(email: string): void {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.addConnection(this.id, email, useMongo).subscribe(success => {
      if (success) {
        this.service.getConnections(this.id, useMongo).subscribe(data => {
          this.connections = data;
        });
      }
      else {
        this.snackBar.open(`User does not exist`, "Ok");
      }
    });
  }

  sendTopicInterestRequest(topic: string): void {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.addInterestedTopic(this.id, topic, useMongo).subscribe(success => {
      if (success) {
        this.service.getInterestedTopics(this.id, useMongo).subscribe(data => {
          this.topics = data;
        });
        this.snackBar.open(`You are following topic "${topic}"!`, "Ok");
      }
      else {
        this.snackBar.open(`${topic} is not available in the database`, "Ok");
      }
    });
  }

  onMultipleArticleSelectionChange(): void {
    let articlesToDisplay: Article[] = [];
    const useMongo = this.cookieService.get("useMongo") === "1";
    let articlesByUser = this.service.getJournalistArticles([{id: this.id}], useMongo);
    let articlesByConnections = this.service.getJournalistArticles(this.connections, useMongo);
    let articlesByTopic = this.service.getTopicArticles(this.topics, useMongo);
    articlesByUser.pipe(combineLatestWith(articlesByConnections, articlesByTopic)).subscribe(([byUser, byConnection, byTopic]) => {
      if (this.byPersonal) {
        articlesToDisplay = byUser;
      }
      if (this.byConnections) {
        byConnection.forEach(articleToAdd => {
          let addArticle = true;
          articlesToDisplay.forEach(article => {
            if (article.shortDescription == articleToAdd.shortDescription && article.number == articleToAdd.number) {
              addArticle = false;
            }
          });
          if (addArticle) {
            articlesToDisplay.push(articleToAdd);
          }
        });
      }
      if (this.byTopics) {
        byTopic.forEach(articleToAdd => {
          let addArticle = true;
          articlesToDisplay.forEach(article => {
            if (article.shortDescription == articleToAdd.shortDescription && article.number == articleToAdd.number) {
              addArticle = false;
            }
          });
          if (addArticle) {
            articlesToDisplay.push(articleToAdd);
          }
        });
      }
      this.articles = articlesToDisplay;
    });
  }

  openArticleDialog() {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.getJournalistById(this.id, useMongo).subscribe(journalist => {
      const dialogRef = this.dialog.open(ArticleEditModalComponent, {
        data: {article: {content: "", authors: [journalist], topics: [], anonymous: false, shortDescription: ""},
          isOwnArticle: true,
          connections: this.connections},
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          result.useMongo = this.cookieService.get("useMongo") === "1";
          this.articleService.sendArticle(result).subscribe(data => {
            if (data) {
              this.snackBar.open("Article successfully published", "Ok");
              this.onMultipleArticleSelectionChange();
            }
          });
        }
      });
    });
  }
}
