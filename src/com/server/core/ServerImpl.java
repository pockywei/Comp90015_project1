package com.server.core;

import com.server.ServerSettings;
import com.server.beans.ServerInfo;
import com.utils.log.CrashHandler;

public class ServerImpl extends ServerControl {

    private static ServerImpl instance = null;

    public synchronized static ServerImpl getInstance() {
        if (instance == null) {
            instance = new ServerImpl();
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
            CrashHandler.getInstance().exit();
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
