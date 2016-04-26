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

    public static final String INVALIED_ERROR = "Invalied message or JSON parse error while parsing message.";
    public static final String AUTH_FAIL = "the supplied secret is incorrect.";
    public static final String LOGIN_FAIL = "attempt to login with wrong info.";
    public static final String REGISTER_FAIL = "the username is already resgistered with the system.";
    public static final String REGISTER_SUCC = "register success.";
    public static final String LOGIN_SUCC = "login success.";

    public static final int SOCKET_TIME_OUT_LIMIT = 10 * 1000;

    public static boolean isRedirect(int localLoad, int remoteLoad) {
        return (localLoad - remoteLoad) >= 2;
    }
}
