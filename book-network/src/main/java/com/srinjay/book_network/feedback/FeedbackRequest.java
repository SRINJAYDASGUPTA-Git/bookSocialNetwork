package com.srinjay.book_network.feedback;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
        @Positive(message = "200")
                @Min (message = "201", value = 0)
                @Max (message = "202", value = 5)
        Double rating,
        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,
        @NotNull(message = "204")
        Long bookId
) {
}
