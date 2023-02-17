import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'connection-dialog',
  templateUrl: './connection-dialog.component.html',
  styleUrls: ['./connection-dialog.component.css']
})
export class ConnectionDialogComponent {

  data = "";

  constructor(public dialogRef: MatDialogRef<ConnectionDialogComponent>){ }

  onNoClick(): void {
    this.data = "";
    this.dialogRef.close();
  }
}
