import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';
import { PageResponseBookResponse } from '../../../../services/models';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss',
})
export class BookListComponent implements OnInit {
   page: number = 0;
   size: number = 2 ;
   bookResponse: PageResponseBookResponse = {};
  constructor(private bookService: BookService, private router: Router) {}

  ngOnInit() {
    this.findAllBooks();
  }
  private findAllBooks() {
    this.bookService.findAllBooks({
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

  gotoFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  gotoPreviousPage() {
    this.page = this.page - 1;
    this.findAllBooks();
  }

  gotoPage(index: number) {
    this.page = index;
    this.findAllBooks();
  }

  gotoNextPage() {
    this.page = this.page + 1;
    this.findAllBooks();

  }

  gotoLastPage() {
    this.page = this.bookResponse.totalPages as number -1;
    this.findAllBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number -1;
  }
}
