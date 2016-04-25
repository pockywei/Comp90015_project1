package com.utils.protocal;

public class Protocal {

    public static final String USER_NAME = "username";
    public static final String COMMAND = "command";
    public static final String SECRET = "secret";
    public static final String SERVER = "server";
    public static final String INFO = "info";
    public static final String SERVER_ID = "id";
    public static final String SERVER_LOAD = "load";
    public static final String SERVER_HOST_NAME = "hostname";
    public static final String SERVER_PORT = "port";
    public static final String ACTIVITY = "activity";
    public static final String ANONYMOUS = "anonymous";

    public static final int SOCKET_TIME_OUT_LIMIT = 10 * 1000;

    public static boolean isRedirect(int localLoad, int remoteLoad) {
        return (localLoad - remoteLoad) >= 2;
    }
}
