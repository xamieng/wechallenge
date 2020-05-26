import { Component, OnInit } from '@angular/core';
import { Review } from '../model/review';
import { ReviewService } from '../service/review.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './review-list.component.html'
})
export class ReviewListComponent implements OnInit {

  reviews: Review[];
  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let keyword = params["keyword"]
      this.reviewService.search(keyword).subscribe(
        data => this.reviews = data,
        () => alert("Search not found: " + keyword))
    });
  }
}
