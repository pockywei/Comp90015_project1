package com.server;

import com.beans.ServerInfo;
import com.server.core.LocalStorage;
import com.utils.UtilHelper;
import com.utils.log.Log;

public class ServerSettings {
    private static final Log log = Log.getInstance();
    private static final String ERROR_INFO = "supplied port %d is out of range, using %d";
    private static final int MAX_PORT_RANGE = 65535;
    private static ServerInfo localInfo = new ServerInfo();
    private static ServerInfo remoteInfo = new ServerInfo();
    private static int activityInterval = 5000; // milliseconds

    public static void setInfo(int localPort, String localHost, int remotePort,
            String remoteHost, String remoteSecret, int activityInterval) {
        if (verifyPort(localPort) || verifyPort(remotePort)) {
            log.error(String.format(ERROR_INFO, localPort, getLocalPort()));
            return;
        }
        ServerSettings.localInfo.setId(UtilHelper.getServerID());
        ServerSettings.localInfo.setPort(localPort);
        ServerSettings.localInfo.setHostname(localHost);
        ServerSettings.localInfo.setSecret(UtilHelper.getSecret());
        ServerSettings.remoteInfo.setPort(remotePort);
        ServerSettings.remoteInfo.setHostname(remoteHost);
        ServerSettings.remoteInfo.setSecret(remoteSecret);
        LocalStorage.getInstance().addServer(remoteInfo);
        ServerSettings.activityInterval = activityInterval;
    }

    public static void setLocalLoad(final int load) {
        localInfo.setLoad(load);
    }

    public static int getLocalPort() {
        return localInfo.getPort();
    }

    public static String getLocalSecret() {
        return localInfo.getSecret();
    }

    public static String getLocalHostname() {
        return localInfo.getHostname();
    }

    public static int getLocalLoad() {
        return localInfo.getLoad();
    }

    public static String getLocalID() {
        return localInfo.getId();
    }

    public static int getRemotePort() {
        return remoteInfo.getPort();
    }

    public static String getRemoteSecret() {
        return remoteInfo.getSecret();
    }

    public static String getRemoteHost() {
        return remoteInfo.getHostname();
    }

    public static int getActivityInterval() {
        return activityInterval;
    }

    private static boolean verifyPort(int port) {
        return port < 0 || port > MAX_PORT_RANGE;
    }
}
