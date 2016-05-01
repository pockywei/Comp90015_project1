package com.protocal;

public class Activity {

    private String message = "";
    private String username = "";

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
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
        return String.format(Protocal.SEND_MESSAGE, username, message);
    }
}
