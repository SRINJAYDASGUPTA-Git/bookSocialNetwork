import {Component, OnInit} from '@angular/core';
import {BookRequest} from "../../../../services/models/book-request";
import {BookService} from "../../../../services/services/book.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit {
  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;
  bookRequest: BookRequest = {author: "", isbn: "", synopsis: "", title: ""};
  constructor(
    private bookService: BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
  }
  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if (bookId) {
      console.log(bookId);
      this.bookService.findBookById({
        "book-id": bookId
      }).subscribe({
        next: (book) => {

          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            author: book.author as string,
            isbn: book.isbn as string,
            synopsis: book.synopsis as string,
            shareable: book.shareable,

          }
          if (book.cover) {
            this.selectedPicture = `data:image/jpeg;base64,${book.cover}`;
          }
        }
      })
    }
  }
  onFileSelected(file: any) {
    this.selectedBookCover = file.target.files[0];
    console.log(this.selectedBookCover);
    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next:(bookId)=>{
        this.bookService.uploadBookCoverPicture({
          'book-id': bookId,
          body:{
            file: this.selectedBookCover
          }
        }).subscribe({
          next:()=>{
            this.router.navigate(['/books/my-books']);
          }
        })
      },
      error:(error)=>{
        this.errorMsg = error.error.validationErrors;
      }
    })
  }

}
