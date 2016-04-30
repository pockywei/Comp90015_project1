package com.server.core;

import com.beans.ServerInfo;
import com.server.ServerSettings;
import com.utils.log.CrashHandler;

public class ServerManager extends AbstractServer {

    private static ServerManager instance = null;

    public synchronized static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    @Override
    public void initServer() {
        try {
//            request(new Socket(ServerSettings.getRemoteHost(),
//                    ServerSettings.getRemotePort()), Command.AUTHENTICATE);
            log.info("sending an authenticate to remote server");
        }
        catch (Exception e) {
            log.error("failed to make connection to "
                    + ServerSettings.getRemoteHost() + ":"
                    + ServerSettings.getRemotePort() + " :" + e);
            CrashHandler.getInstance().errorExit();
        }
    }

    @Override
    public void serverAnnounce() {
        // final List<ServerInfo> serverList = DataTable.getInstance()
        // .getRemoteServers();
    }

    public ServerInfo redirect() {
        // Find which server should be connected to.
        // TODO

        return null;
    }
}
