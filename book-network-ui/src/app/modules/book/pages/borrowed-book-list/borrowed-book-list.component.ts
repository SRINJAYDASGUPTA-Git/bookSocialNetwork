import { Component, OnInit } from '@angular/core';
import { BorrowedBookResponse } from '../../../../services/models/borrowed-book-response';
import { PageResponseBorrowedBookResponse } from '../../../../services/models/page-response-borrowed-book-response';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';
import { FeedbackRequest } from '../../../../services/models/feedback-request';
import { FeedbacksService } from '../../../../services/services';

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss',
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  page = 0;
  size = 10;
  feedbackRequest: FeedbackRequest = { bookId: 0, comment: '', rating: 0 };
  selectedBook: BorrowedBookResponse | undefined = undefined;
  constructor(
    private bookService: BookService,
    private router: Router,
    private feedbackService: FeedbacksService
  ) {}
  ngOnInit() {
    this.findBorrowedBooks();
    if(this.selectedBook)
      console.log(this.selectedBook)
  }

  findBorrowedBooks() {
    this.bookService
      .findAllBorrowedBooks({
        page: this.page,
        size: this.size,
      })
      .subscribe({
        next: (response: PageResponseBorrowedBookResponse) => {
          this.borrowedBooks = response;
          console.log(this.borrowedBooks)
        },
      });
  }

  returnBook(withFeedback: boolean) {
    this.bookService
      .returnBook({
        'book-id': this.selectedBook?.id as number,
      })
      .subscribe({
        next: () => {
          console.log(this.selectedBook?.id)
          if (withFeedback) {
            this.giveFeedback();
          }
          this.selectedBook = undefined;
          this.findBorrowedBooks();
        },
      });
  }
  giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {
        this.feedbackRequest = { bookId: 0, comment: '', rating: 0 };
      }
    });
  }
  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    console.log(book);
    this.feedbackRequest.bookId = book.id as number;
  }

  get isLastPage(): boolean {
    return this.page == (this.borrowedBooks.totalPages as number) - 1;
  }

  gotoFirstPage() {
    this.page = 0;
    this.findBorrowedBooks();
  }

  gotoPreviousPage() {
    this.page = this.page - 1;
    this.findBorrowedBooks();
  }

  gotoPage(index: number) {
    this.page = index;
    this.findBorrowedBooks();
  }

  gotoNextPage() {
    this.page = this.page + 1;
    this.findBorrowedBooks();
  }

  gotoLastPage() {
    this.page = (this.borrowedBooks.totalPages as number) - 1;
    this.findBorrowedBooks();
  }
}
