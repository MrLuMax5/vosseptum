import {Component, Input} from '@angular/core';
import {EditorialBoard} from "../../../modules/vosssmolina-types";

@Component({
  selector: 'editorial-board-table',
  templateUrl: './editorial-board-table.component.html',
  styleUrls: ['./editorial-board-table.component.css']
})
export class EditorialBoardTableComponent {

  @Input() boards: EditorialBoard[] = [];
  displayedColumns: string[] = ['reputation', 'institution'];
}
