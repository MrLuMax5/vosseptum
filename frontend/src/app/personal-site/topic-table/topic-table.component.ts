import {Component, Input} from '@angular/core';
import {Topic} from "../../../modules/vosssmolina-types";

@Component({
  selector: 'topic-table',
  templateUrl: './topic-table.component.html',
  styleUrls: ['./topic-table.component.css']
})
export class TopicTableComponent {

  @Input() topics: Topic[] = [];
  displayedColumns: string[] = ['subject', 'popularity'];

}
