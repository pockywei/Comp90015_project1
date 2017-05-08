package com.utils.log.exception;

public class ListeningException extends Exception {

    private static final long serialVersionUID = 1L;

    public ListeningException() {
        super("Server Listening stop, shutdown the system.");
    }
}
