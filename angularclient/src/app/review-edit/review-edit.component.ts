import { Component, OnInit } from '@angular/core';
import { ReviewEdit } from '../model/review-edit';
import { ReviewService } from '../service/review.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './review-edit.component.html'
})
export class ReviewEditComponent implements OnInit {

  id: String;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reviewService: ReviewService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params["id"]
      let reviewText = prompt("Please enter your updated review")
      let review = new ReviewEdit()
      review.review = reviewText
      if (reviewText != "" && reviewText != null) {
        this.reviewService.update(this.id, review).subscribe(
          () => {
            this.router.navigate(['/review/' + this.id]);
          },
          () => alert("Review not found: " + this.id)
        )
      } else {
        this.router.navigate(['/review/' + this.id]);
      }
    })
  }
}
