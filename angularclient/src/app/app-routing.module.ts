import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ReviewListComponent } from './review-list/review-list.component';
import { ReviewComponent } from './review/review.component';
import { ReviewEditComponent } from './review-edit/review-edit.component';

const routes: Routes = [
  { path: 'review/:id', component: ReviewComponent },
  { path: 'search/:keyword', component: ReviewListComponent },
  { path: 'review/edit/:id', component: ReviewEditComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
