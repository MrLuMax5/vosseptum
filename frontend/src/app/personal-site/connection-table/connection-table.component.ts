import {Component, Input} from '@angular/core';
import {Journalist} from "../../../modules/vosssmolina-types";

@Component({
  selector: 'connection-table',
  templateUrl: './connection-table.component.html',
  styleUrls: ['./connection-table.component.css']
})
export class ConnectionTableComponent {

  @Input() connections: Journalist[] = [];
  displayedColumns: string[] = ['id', 'email'];
}
