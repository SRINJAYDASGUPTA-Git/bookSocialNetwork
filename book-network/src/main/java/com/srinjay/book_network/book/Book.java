package com.srinjay.book_network.book;


import com.srinjay.book_network.common.BaseEntity;
import com.srinjay.book_network.feedback.Feedback;
import com.srinjay.book_network.history.BookTransactionHistory;
import com.srinjay.book_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Book extends BaseEntity {

    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate(){
        if(feedbacks == null || feedbacks.isEmpty()){
            return 0;
        }

        var rate = this.feedbacks.stream ().mapToDouble (Feedback::getNote).average ().orElse (0.0);
        return Math.round (rate * 10.0) / 10.0;
    }
}
