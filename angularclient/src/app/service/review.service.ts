import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Review } from '../model/review';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ReviewService {

  private readonly usersUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/reviews/';
  }

  public find(id): Observable<Review> {
    return this.http.get<Review>(this.usersUrl + id);
  }

  public search(keyword) {
    return this.http.get<Review[]>(this.usersUrl + "?query=" + keyword);
  }

  public update(id, review) {
    return this.http.put<Review>(this.usersUrl + id, review);
  }
}
