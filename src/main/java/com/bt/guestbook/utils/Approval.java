package com.bt.guestbook.utils;

public enum Approval {
    APPROVE("APPROVED"), UNAPPROVED("UNAPPROVED");

    private String value;

    Approval(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
