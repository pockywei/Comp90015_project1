package com.protocal;

public class Activity {

    private String message = "";
    private String username = "";
    private boolean isLeft = true;

    public Activity() {
    }

    public Activity(String user, String message, boolean isLeft) {
        this.username = user;
        this.message = message;
        this.isLeft = isLeft;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public Activity setMessage(String message) {
        this.message = message;
        return this;
    }

    public Activity setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String toString() {
        return username + Protocal.RECEIVE_MESSAGE_TAG + message;
    }
}
