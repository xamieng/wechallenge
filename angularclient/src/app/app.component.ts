import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {

  title: string;
  reviewId: String;
  keyword: String;

  constructor() {
    this.title = 'WeChallenge Review Application';
  }
}
