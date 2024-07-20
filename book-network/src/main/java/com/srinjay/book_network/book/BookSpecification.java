package com.srinjay.book_network.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> withOwnerId(Long ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
