import {Component, OnInit} from '@angular/core';
import {ReportService} from "../shared/report-service.service";
import {Journalist, TruthfulnessReport} from "../../modules/vosssmolina-types";
import {MatDialog} from "@angular/material/dialog";
import {TruthfulnessReportEditModalComponent} from "../truthfulness-report-edit-modal/truthfulness-report-edit-modal.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CookieService} from "ngx-cookie-service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'truthfulness-report-modal',
  templateUrl: './truthfulness-report-modal.component.html',
  styleUrls: ['./truthfulness-report-modal.component.css']
})
export class TruthfulnessReportModalComponent implements OnInit {

  articleNumber!: number;
  shortDescription!: string;
  reports: TruthfulnessReport[] = [];
  displayedColumns: string[] = ['authors', 'grade', 'content'];
  connections: Journalist[] = [];
  id: number;

  constructor(private route: ActivatedRoute,
              private cookieService: CookieService,
              private service: ReportService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
    this.id = Number(cookieService.get("login"));
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.getConnections(this.id, useMongo).subscribe(data => {
      this.connections = data;
    });
  }

  ngOnInit(): void {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.route.params.subscribe(params => {
      this.shortDescription = params['shortDescription'];
      this.articleNumber = params['articleNumber'];
      if (this.articleNumber && this.shortDescription) {
        this.service.getReportsForArticleURL(
          {shortDescription: this.shortDescription, number: this.articleNumber}, useMongo)
          .subscribe(reports => {
            this.reports = reports;
          });
      }
    });
  }

  addReportDialog() {
    const useMongo = this.cookieService.get("useMongo") === "1";
    this.service.getJournalistById(this.id, useMongo).subscribe(journalist => {

      const dialogRef = this.dialog.open(TruthfulnessReportEditModalComponent, {
        data: {connections: this.connections,
          report: {articleNumber: this.articleNumber, articleShortDescription: this.shortDescription, content: "", grade: 0, authors: [journalist]}},
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.service.sendReport(result, useMongo).subscribe(data => {
            if (data) {
              this.snackBar.open("Report successfully added to database", "Ok");
              this.service.getReportsForArticleURL(
                {shortDescription: this.shortDescription, number: this.articleNumber}, useMongo)
                .subscribe(reports => {
                  this.reports = reports;
                });
            }
          });
        }
      });
    });
  }
}
