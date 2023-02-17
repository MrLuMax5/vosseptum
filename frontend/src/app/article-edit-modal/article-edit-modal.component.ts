import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ArticleEditData, Journalist, Topic} from "../../modules/vosssmolina-types";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatChipInputEvent} from "@angular/material/chips";

@Component({
  selector: 'article-edit-modal',
  templateUrl: './article-edit-modal.component.html',
  styleUrls: ['./article-edit-modal.component.css']
})
export class ArticleEditModalComponent {

  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  isOwnArticle = false;
  shortDescriptionNotSet: boolean;
  selectedJournalistToAdd!: Journalist | undefined;

  constructor(public dialogRef: MatDialogRef<ArticleEditModalComponent>,
              @Inject(MAT_DIALOG_DATA) public articleData: ArticleEditData) {
    this.isOwnArticle = articleData.isOwnArticle;
    this.shortDescriptionNotSet = !articleData.article.shortDescription;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  // openReportDialog() {
  //   this.reportService.articleNumber = this.articleData.article.number;
  //   this.reportService.shortDescription = this.articleData.article.shortDescription;
  //   this.router.navigateByUrl("/reports")
  // }

  removeAuthor(author: Journalist) {
    const index = this.articleData.article.authors.indexOf(author, 0);
    if (index > -1) {
      this.articleData.article.authors.splice(index, 1);
    }
  }

  addJournalistToAuthors() {
    if (this.selectedJournalistToAdd != undefined) {
      this.articleData.article.authors.push(this.selectedJournalistToAdd);
    }
    this.selectedJournalistToAdd = undefined;
  }

  removeTopic(topic: Topic) {
    const index = this.articleData.article.topics.indexOf(topic);
    if (index > -1) {
      this.articleData.article.topics.splice(index, 1);
    }
  }

  addTopic(event: MatChipInputEvent) {
    const value = (event.value || '').trim();
    if (value) {
      this.articleData.article.topics.push({subject: value})
    }
    event.chipInput!.clear();
  }
}
