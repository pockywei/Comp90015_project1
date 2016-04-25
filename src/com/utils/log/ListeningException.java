package com.utils.log;

public class ListeningException extends Exception {

    public ListeningException() {
        super("Server Listening stop, shutdown the system.");
    }
}
