import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import {HttpClientModule} from "@angular/common/http";
import { AppComponent } from './app.component';
import { MainpageComponent } from './mainpage/mainpage.component';
import {RouterModule, Routes} from "@angular/router";
import {MatButtonModule} from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationComponent } from './registration/registration.component';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {MatToolbarModule} from "@angular/material/toolbar";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { LoginComponent } from './login/login.component';
import { ToolbarComponent } from './toolbar/toolbar.component';
import { PersonalSiteComponent } from './personal-site/personal-site.component';
import {AuthGuardService} from "./auth-guard.service";
import { CookieService } from 'ngx-cookie-service';
import { ConnectionTableComponent } from './personal-site/connection-table/connection-table.component';
import {MatTableModule} from "@angular/material/table";
import { ConnectionDialogComponent } from './personal-site/connection-dialog/connection-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import { ArticleTableComponent } from './personal-site/article-table/article-table.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import { TopicTableComponent } from './personal-site/topic-table/topic-table.component';
import { TopicDialogComponent } from './personal-site/topic-dialog/topic-dialog.component';
import { EditorialBoardTableComponent } from './personal-site/editorial-board-table/editorial-board-table.component';
import { ArticleEditModalComponent } from './article-edit-modal/article-edit-modal.component';
import {MatIconModule} from "@angular/material/icon";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSelectModule} from "@angular/material/select";
import {MatChipsModule} from "@angular/material/chips";
import { TruthfulnessReportModalComponent } from './truthfulness-report-modal/truthfulness-report-modal.component';
import { TruthfulnessReportEditModalComponent } from './truthfulness-report-edit-modal/truthfulness-report-edit-modal.component';
import { TrustworthyComponent } from './trustworthy/trustworthy.component';
import {MatRadioModule} from "@angular/material/radio";
import { PopularityComponent } from './popularity/popularity.component';

const routes: Routes = [
  {path: '', component: MainpageComponent},
  {path: 'register', component: RegistrationComponent},
  {path: 'login', component: LoginComponent},
  {path: 'personal-site', component: PersonalSiteComponent, canActivate: [AuthGuardService]},
  {path: 'reports/:shortDescription/:articleNumber', component: TruthfulnessReportModalComponent},
  {path: 'trustworthy', component: TrustworthyComponent},
  {path: 'popularity', component: PopularityComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    MainpageComponent,
    RegistrationComponent,
    LoginComponent,
    ToolbarComponent,
    PersonalSiteComponent,
    ConnectionTableComponent,
    ConnectionDialogComponent,
    ArticleTableComponent,
    TopicTableComponent,
    TopicDialogComponent,
    EditorialBoardTableComponent,
    ArticleEditModalComponent,
    TruthfulnessReportModalComponent,
    TruthfulnessReportEditModalComponent,
    TrustworthyComponent,
    PopularityComponent
  ],
	imports: [
		RouterModule.forRoot(routes),
		BrowserModule,
		BrowserAnimationsModule,
		HttpClientModule,
		MatButtonModule,
		MatCardModule,
		MatInputModule,
		MatToolbarModule,
		ReactiveFormsModule,
		MatSnackBarModule,
		MatGridListModule,
		MatTableModule,
		MatDialogModule,
		FormsModule,
		MatCheckboxModule,
		MatIconModule,
		MatTooltipModule,
		MatSelectModule,
		MatChipsModule,
		MatRadioModule
	],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
