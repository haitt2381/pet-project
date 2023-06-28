package com.pp.common.constant;

public enum Role {
    MEMBER("member"), MODERATOR("moderator"), ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}