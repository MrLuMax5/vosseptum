import {Component, Inject} from '@angular/core';
import {Journalist, TruthfulnessReportData} from "../../modules/vosssmolina-types";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ArticleEditModalComponent} from "../article-edit-modal/article-edit-modal.component";

@Component({
  selector: 'app-truthfulness-report-edit-modal',
  templateUrl: './truthfulness-report-edit-modal.component.html',
  styleUrls: ['./truthfulness-report-edit-modal.component.css']
})
export class TruthfulnessReportEditModalComponent {

  selectedJournalistToAdd!: Journalist | undefined;
  grades = [1, 2, 3, 4, 5, 6]

  constructor(public dialogRef: MatDialogRef<ArticleEditModalComponent>, @Inject(MAT_DIALOG_DATA) public reportData: TruthfulnessReportData) {
  }

  removeAuthor(author: Journalist) {
    const index = this.reportData.report.authors.indexOf(author, 0);
    if (index > -1) {
      this.reportData.report.authors.splice(index, 1);
    }
  }

  addJournalistToAuthors() {
    if (this.selectedJournalistToAdd != undefined) {
      this.reportData.report.authors.push(this.selectedJournalistToAdd);
    }
    this.selectedJournalistToAdd = undefined;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
