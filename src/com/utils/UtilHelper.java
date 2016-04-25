package com.utils;

import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

public class UtilHelper {
    private static SecureRandom random = new SecureRandom();

    public static String getSocketAddr(Socket socket) {
        return socket.getInetAddress() + ":" + socket.getPort();
    }

    public static String getSecret() {
        return new BigInteger(130, random).toString(32);
    }

    public static boolean isEmptyStr(String s) {
        if (s != null && s.length() != 0) {
            return false;
        }
        return true;
    }
}
