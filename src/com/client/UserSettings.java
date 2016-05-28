package com.client;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Protocal;

public class UserSettings {

    private final static UserInfo user = new UserInfo();
    // default Aaron's remote server.
    private final static ServerInfo remoteServer = new ServerInfo()
            .setHostname("sunrise.cis.unimelb.edu.au").setPort(5124);

    public static void setUser(String username, String secret) {
        synchronized (UserSettings.user) {
            UserSettings.user.setUsername(username);
            UserSettings.user.setSecret(secret);
        }
    }

    public static void setServerInfo(int remotePort, String remoteHost) {
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

    public static int getSSLRemotePort() {
        return Protocal.getSSLPort(remoteServer.getPort());
    }

    public static String getRemoteHost() {
        return remoteServer.getHostname();
    }

    public static void logoutUser() {
        setUser("", "");
    }
}
