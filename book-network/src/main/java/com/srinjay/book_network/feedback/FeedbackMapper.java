package com.srinjay.book_network.feedback;

import com.srinjay.book_network.book.Book;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder ()
                .note (request.rating ())
                .comment (request.comment ())
                .book (Book.builder ()
                        .id (request.bookId ())
                        .archived (false)
                        .shareable (false)
                        .build ())
                .build ();
    }
}
