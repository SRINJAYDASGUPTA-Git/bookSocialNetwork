package com.srinjay.book_network.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


public enum BusinessErrorCodes {
    NO_CODE(0, "No Code", NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Current Password is incorrect", BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH(301, "New Password does not match", BAD_REQUEST),
    ACCOUNT_LOCKED(302, "User account Locked", FORBIDDEN),
    ACCOUNT_DISABLED(303, "User account Disabled", FORBIDDEN),
    BAD_CREDENTIALS(304, "Username and/or Password incorrect", FORBIDDEN),
    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
