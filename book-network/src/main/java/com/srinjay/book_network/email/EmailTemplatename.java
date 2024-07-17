package com.srinjay.book_network.email;

import lombok.Getter;

@Getter
public enum EmailTemplatename {

    ACTIVATE_ACCOUNT("activate_account")

    ;

    private final String name;

    EmailTemplatename(String name) {
        this.name = name;
    }
}
