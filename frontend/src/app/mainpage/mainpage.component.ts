import { Component, OnInit } from '@angular/core';
import {MainpageService} from "./mainpage.service";
import {FillResponse} from "../../modules/vosssmolina-types";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'mainpage',
  templateUrl: './mainpage.component.html',
  styleUrls: ['./mainpage.component.css']
})
export class MainpageComponent implements OnInit{

  datafillResponse!: FillResponse;

  constructor(private service: MainpageService, private snackBar: MatSnackBar) {
    this.datafillResponse = {entityCount: 0, bridgeCount: 0}
  }

  ngOnInit(): void {
    this.service.getEntityAndBridgeCount().subscribe({
      next: (data) => {
        this.datafillResponse = data;
      }
    });
  }

  fillData(): void {
    this.service.fillDataCall().subscribe({
      next: (data) => {
        this.datafillResponse = data;
      },
    });
  }

  migrateData() {
    this.service.migrateData().subscribe(success => {
      if (success) {
        this.snackBar.open(`Migration successful!`, "Ok");
      }
    })
  }
}
