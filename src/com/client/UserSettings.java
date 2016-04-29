package com.client;

import com.server.beans.ServerInfo;
import com.server.beans.UserInfo;

public class UserSettings {
    private static UserInfo user;
    private static ServerInfo remoteServer;

    public static void setUser(String username, String secret,
            String remoteHost, int remotePort) {
        UserSettings.user = new UserInfo(username, secret);
        UserSettings.remoteServer = new ServerInfo(null, remotePort, remoteHost,
                null);
    }

    public static void resetServerInfo(int remotePort, String remoteHost) {
        synchronized (UserSettings.remoteServer) {
            UserSettings.remoteServer.setHostname(remoteHost);
            UserSettings.remoteServer.setPort(remotePort);
        }
    }

    public static String getUsername() {
        return user.getUsername();
    }

    public static String getSecret() {
        return user.getSecret();
    }

    public static int getRemotePort() {
        return remoteServer.getPort();
    }

    public static String getRemoteHost() {
        return remoteServer.getHostname();
    }
}
