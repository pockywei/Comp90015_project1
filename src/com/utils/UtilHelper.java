package com.utils;

import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.UUID;

import com.protocal.Protocal;

public class UtilHelper {

    private static SecureRandom random = new SecureRandom();

    public static String getSocketAddr(Socket socket) {
        return String.format(Protocal.SOCKET_ADDRESS, socket.getInetAddress(),
                socket.getPort());
    }

    public static String getSecret() {
        return new BigInteger(130, random).toString(32);
    }

    public static String getServerID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 
     * @param s
     * @return true: empty; false: not
     */
    public static boolean isEmptyStr(String s) {
        if (s != null && s.length() != 0) {
            return false;
        }
        return true;
    }
}
