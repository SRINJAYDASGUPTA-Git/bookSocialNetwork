package com.srinjay.book_network.book;

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
}
