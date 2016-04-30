package com.protocal;

public class Activity {
    
    private String message;
    private String sender;

    public String getMessage() {
        return message;
    }

    public Activity setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return message;
    }
}
