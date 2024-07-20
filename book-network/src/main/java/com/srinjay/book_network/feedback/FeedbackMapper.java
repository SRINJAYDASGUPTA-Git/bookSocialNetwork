package com.srinjay.book_network.feedback;

import com.srinjay.book_network.book.Book;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Long id) {
        return FeedbackResponse.builder ()
                .note (feedback.getNote ())
                .comment (feedback.getComment ())
                .ownFeedback (Objects.equals (feedback.getCreatedBy (), id))
                .build ();
    }
}
