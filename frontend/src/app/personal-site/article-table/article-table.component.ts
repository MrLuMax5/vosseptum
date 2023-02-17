import {Component, Input} from '@angular/core';
import {Article, Journalist} from "../../../modules/vosssmolina-types";
import {MatDialog} from "@angular/material/dialog";
import {ArticleEditModalComponent} from "../../article-edit-modal/article-edit-modal.component";
import {ArticleService} from "../../shared/article-service.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'article-table',
  templateUrl: './article-table.component.html',
  styleUrls: ['./article-table.component.css']
})
export class ArticleTableComponent {

  @Input() id!: number;
  @Input() articles: Article[] = [];
  @Input() connections: Journalist[] = [];
  displayedColumns: string[] = ['authors', 'content', 'reports'];
  useMongo = false;

  constructor(private dialog: MatDialog,
              private service: ArticleService,
              private snackBar: MatSnackBar,
              private router: Router,
              private cookieService: CookieService) {
  }

  openArticleDialog(row: Article) {
    let isOwnArticle = false;
    row.authors.forEach(author => {
      if (author.id == this.id) {
        isOwnArticle = true;
      }
    });
    const dialogRef = this.dialog.open(ArticleEditModalComponent, {
      data: {article: structuredClone(row), isOwnArticle: isOwnArticle, connections: this.connections},
    });

    if (isOwnArticle) {
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          result.useMongo = this.cookieService.get("useMongo") === "1";
          this.service.sendArticle(result).subscribe(data => {
            if (data) {
              this.snackBar.open("Article successfully published (Reload page to view)", "Ok");
              row = result;
            }
          });
        }
      });
    }
  }

  viewReportsForArticle(shortDescription: string, articleNumber: number) {
    this.router.navigateByUrl(`/reports/${shortDescription}/${articleNumber}`);
  }
}
