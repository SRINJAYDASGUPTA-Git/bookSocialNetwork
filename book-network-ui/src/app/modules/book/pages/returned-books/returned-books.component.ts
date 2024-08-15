import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {FeedbacksService} from "../../../../services/services/feedbacks.service";

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.scss'
})
export class ReturnedBooksComponent implements OnInit{
  returnedBooks: PageResponseBorrowedBookResponse = {};
  page = 0;
  size = 10;
  message: string = "";
  label: string = 'success';
  constructor(
    private bookService: BookService,
  ) {}
  ngOnInit() {
    this.findAllReturnedBooks();
  }

  findAllReturnedBooks() {
    this.bookService
      .findAllReturnedBooks({
        page: this.page,
        size: this.size,
      })
      .subscribe({
        next: (response: PageResponseBorrowedBookResponse) => {
          this.returnedBooks = response;
          console.log(this.returnedBooks)
        },
      });
  }



  get isLastPage(): boolean {
    return this.page == (this.returnedBooks.totalPages as number) - 1;
  }

  gotoFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  gotoPreviousPage() {
    this.page = this.page - 1;
    this.findAllReturnedBooks();
  }

  gotoPage(index: number) {
    this.page = index;
    this.findAllReturnedBooks();
  }

  gotoNextPage() {
    this.page = this.page + 1;
    this.findAllReturnedBooks();
  }

  gotoLastPage() {
    this.page = (this.returnedBooks.totalPages as number) - 1;
    this.findAllReturnedBooks();
  }

  approveBookReturn(book: BorrowedBookResponse) {
    if(!book.returned){
      return;
    }
    this.bookService.approveReturnBorrowedBook({
      'book-id': book.id as number
    }).subscribe({
      next: ()=>{
        this.message = "Book Return Approved Successfully!"
        this.label = "success"
        this.findAllReturnedBooks()
      },
      error:(error)=>{
        this.message = error.error.error;
        this.label = "error"
      }
    });
  }
}
