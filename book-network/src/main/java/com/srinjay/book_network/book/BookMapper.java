package com.srinjay.book_network.book;

import com.srinjay.book_network.history.BookTransactionHistory;
import com.srinjay.book_network.user.User;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request, User user) {
        return Book.builder ()
                .id (request.id ())
                .title (request.title ())
                .author (request.author ())
                .synopsis (request.synopsis ())
                .archived (false)
                .isbn (request.isbn ())
                .shareable (request.shareable ())
                .build ();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder ()
                .id (book.getId ())
                .title (book.getTitle ())
                .author (book.getAuthor ())
                .isbn (book.getIsbn ())
                .synopsis (book.getSynopsis ())
                .owner (book.getOwner ().getFullName ())
//                TODO: Implement this
//                .cover (book.getBookCover ().getBytes ())
                .rate (book.getRate ())
                .archived (book.isArchived ())
                .shareable (book.isShareable ())
                .build ();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
            return BorrowedBookResponse.builder ()
                    .id (history.getId ())
                    .title (history.getBook ().getTitle ())
                    .author (history.getBook ().getAuthor ())
                    .isbn (history.getBook ().getIsbn ())
                    .rate (history.getBook ().getRate ())
                    .returned (history.isReturned ())
                    .returnApproved (history.isReturnApproved ())
                    .build ();
    }
}
