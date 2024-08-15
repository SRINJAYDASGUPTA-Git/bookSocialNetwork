import { Component } from '@angular/core';
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {BookResponse} from "../../../../services/models/book-response";

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent {
  page: number = 0;
  size: number = 4 ;
  bookResponse: PageResponseBookResponse = {};
  constructor(private bookService: BookService, private router: Router) {}

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number -1;
  }

  ngOnInit() {
    this.findOwnerBooks();
  }

  gotoFirstPage() {
    this.page = 0;
    this.findOwnerBooks();
  }

  gotoPreviousPage() {
    this.page = this.page - 1;
    this.findOwnerBooks();
  }

  gotoPage(index: number) {
    this.page = index;
    this.findOwnerBooks();
  }

  gotoNextPage() {
    this.page = this.page + 1;
    this.findOwnerBooks();

  }

  gotoLastPage() {
    this.page = this.bookResponse.totalPages as number -1;
    this.findOwnerBooks();
  }

  archiveBook(book: BookResponse) {
    this.bookService.updateArchivedStatus({
      'book-id': book.id as number,
    }).subscribe({
      next: ()=>{
        book.archived = !book.archived;
      }
    })
  }

  editBook(book: BookResponse) {
    this.router.navigate(['/books/manage', book.id]).then(r => r);
  }

  shareBook(book: BookResponse) {
    this.bookService.updateShareableStatus({
      'book-id': book.id as number,

    }).subscribe({
        next: ()=>{
          book.shareable = !book.shareable;
        }
      })
  }

  private findOwnerBooks() {
    this.bookService.findAllBooksByOwner({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: (books) => {
        this.bookResponse = books;
        console.log(this.bookResponse);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}
