import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { ReviewListComponent } from './review-list/review-list.component';
import { ReviewService } from './service/review.service';
import {ReviewComponent} from "./review/review.component";
import {ReviewEditComponent} from "./review-edit/review-edit.component";

@NgModule({
  declarations: [
    AppComponent,
    ReviewListComponent,
    ReviewComponent,
    ReviewEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [ReviewService],
  bootstrap: [AppComponent]
})
export class AppModule { }
