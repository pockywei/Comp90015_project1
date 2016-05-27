package com.protocal;

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
    public static final String ACTIVITY_MESSAGE = "message";
    public static final String ACTIVITY_SENDER = "authenticated_user";

    public static final String INVALIED_ERROR = "Invalied message or JSON parse error while parsing message.";
    public static final String AUTH_FAIL = "the supplied secret is incorrect: %s";
    public static final String LOGIN_FAIL = "wrong secret for user %s";
    public static final String REGISTER_FAIL = "the %s is already resgistered with the system.";
    public static final String HAS_AUTH = "the server has been successfully authenticated.";
    public static final String HAS_NOE_AUTH = "the server has not been authenticated.";
    public static final String REGISTER_FAIL_NO_USER = "user %s is not registered";
    public static final String REGISTER_SUCC = "register success for %s";
    public static final String LOGIN_SUCC = "logged in as user %s";
    public static final String ERROR_PARSE = "the message must contain non-null key %s";
    public static final String AUTH_LOGIN_FAIL = "must send a LOGIN message first";
    public static final String CONNECTION_FAIL = "connection error, please check remote info or network.";

    public static final String LOCAL_HOSTNAME = "localhost";
    public static final int LOCAL_PORT = 3780;
    public static final int SOCKET_TIME_OUT_LIMIT = 10 * 1000;
    public static final int WAIT_FOR_LOCK_RESPONSE = 1000;
    public static final String SEND_MESSAGE_TAG = " << ";
    public static final String RECEIVE_MESSAGE_TAG = " >> ";
    public static final String SEND_BY_ME = "Me";
    public static final String SOCKET_ADDRESS = "%s:%d";

    public static boolean isRedirect(int localLoad, int remoteLoad) {
        return (localLoad - remoteLoad) >= 2;
    }
}
