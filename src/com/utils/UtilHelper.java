package com.utils;

import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.UUID;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public static String toHTML(String aText) {
        final StringBuilder result = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(
                aText);
        char character = iterator.current();
        while (character != CharacterIterator.DONE) {
            if (character == '\n') {
                result.append("<br/>");
            }
            else {
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }

    public static String getPrettyJson(String jsonStr) {
        JsonObject json = isJsonObject(jsonStr);
        if (json == null) {
            return jsonStr;
        }
        return getPrettyJson(json);
    }

    public static String getPrettyJson(JsonObject o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }

    public static JsonObject isJsonObject(String str) {
        try {
            return new JsonParser().parse(str).getAsJsonObject();
        }
        catch (Exception e) {
            return null;
        }
    }
}
