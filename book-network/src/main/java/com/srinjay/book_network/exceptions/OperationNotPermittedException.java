package com.srinjay.book_network.exceptions;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String msg) {
        super(msg);
    }
}
