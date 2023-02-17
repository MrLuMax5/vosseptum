import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Topic} from "../../../modules/vosssmolina-types";

@Component({
  selector: 'topic-dialog',
  templateUrl: './topic-dialog.component.html',
  styleUrls: ['./topic-dialog.component.css']
})
export class TopicDialogComponent {

  data = "";

  constructor(public dialogRef: MatDialogRef<TopicDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public topics: Topic[]){ }

  onNoClick(): void {
    this.data = "";
    this.dialogRef.close();
  }

}
