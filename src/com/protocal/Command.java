package com.protocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Command {

    /*
     * Client request
     */
    LOGIN,

    LOGOUT,

    ACTIVITY_MESSAGE,

    REGISTER,

    /*
     * Server request
     */
    AUTHENTICATE,

    SERVER_ANNOUNCE,

    ACTIVITY_BROADCAST,

    LOCK_REQUEST,

    /*
     * All response
     */
    INVALID_MESSAGE,

    AUTHENTICATION_FAIL,

    LOGIN_SUCCESS,

    LOGIN_FAILED,

    REDIRECT,

    REGISTER_FAILED,

    REGISTER_SUCCESS,

    LOCK_DENIED,

    LOCK_ALLOWED;

    private static final List<Command> USER_REQUEST = new ArrayList<>();
    private static final List<Command> SERVER_REQUEST = new ArrayList<>();
    private static final Map<String, Command> map = new HashMap<>();

    static {
        USER_REQUEST.add(LOGIN);
        USER_REQUEST.add(LOGOUT);
        USER_REQUEST.add(ACTIVITY_MESSAGE);
        USER_REQUEST.add(REGISTER);

        SERVER_REQUEST.add(AUTHENTICATE);
        SERVER_REQUEST.add(SERVER_ANNOUNCE);
        SERVER_REQUEST.add(ACTIVITY_BROADCAST);
        SERVER_REQUEST.add(LOCK_REQUEST);

        map.put("INVALID_MESSAGE", INVALID_MESSAGE);
        map.put("AUTHENTICATE", AUTHENTICATE);
        map.put("AUTHENTICATION_FAIL", AUTHENTICATION_FAIL);
        map.put("LOGIN", LOGIN);
        map.put("LOGIN_SUCCESS", LOGIN_SUCCESS);
        map.put("LOGIN_FAILED", LOGIN_FAILED);
        map.put("LOGOUT", LOGOUT);
        map.put("REDIRECT", REDIRECT);
        map.put("SERVER_ANNOUNCE", SERVER_ANNOUNCE);
        map.put("ACTIVITY_MESSAGE", ACTIVITY_MESSAGE);
        map.put("ACTIVITY_BROADCAST", ACTIVITY_BROADCAST);
        map.put("REGISTER", REGISTER);
        map.put("REGISTER_FAILED", REGISTER_FAILED);
        map.put("REGISTER_SUCCESS", REGISTER_SUCCESS);
        map.put("LOCK_REQUEST", LOCK_REQUEST);
        map.put("LOCK_DENIED", LOCK_DENIED);
        map.put("LOCK_ALLOWED", LOCK_ALLOWED);
    }

    public static boolean isClientRequest(Command com) {
        return USER_REQUEST.contains(com);
    }

    public static boolean isServerRequest(Command com) {
        return SERVER_REQUEST.contains(com);
    }

    public static Command getCommand(String command) {
        return map.get(command);
    }

    public boolean equals(String command) {
        return this.toString().equals(command);
    }
}