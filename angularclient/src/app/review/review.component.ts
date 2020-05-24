import { Component, OnInit } from '@angular/core';
import { Review } from '../model/review';
import { ReviewService } from '../service/review.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './review.component.html'
})
export class ReviewComponent implements OnInit {

  review: Review;
  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let id = params["id"]
      this.reviewService.find(id).subscribe(
        data => {
          this.review = data;
        }, () => alert("Review not found: " + id)
      )
    });
  }
}
