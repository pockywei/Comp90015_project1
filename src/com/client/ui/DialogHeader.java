package com.client.ui;

public class DialogHeader {

    private String name;
    private boolean isPassword;
    private String text;

    public DialogHeader(String name) {
        this(name, false);
    }

    public DialogHeader(String name, boolean isPassword) {
        this(name, isPassword, null);
    }

    public DialogHeader(String name, String text) {
        this(name, false, text);
    }

    public DialogHeader(String name, boolean isPassword, String text) {
        this.name = name;
        this.isPassword = isPassword;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public String getText() {
        return text;
    }
}
