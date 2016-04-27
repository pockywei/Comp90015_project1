package com.server.core;

import java.io.IOException;
import java.net.Socket;

import com.protocal.Command;
import com.protocal.connection.Connection;
import com.protocal.connection.Request;
import com.protocal.connection.Response;
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
            request(new Socket(ServerSettings.getRemoteHost(),
                    ServerSettings.getRemotePort()), Command.AUTHENTICATE);
            log.info("sending an authenticate to remote server");
        }
        catch (IOException e) {
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

    @Override
    public Connection response(Socket s) throws IOException {
        Response c = (Response) super.response(s);
        return c;
    }

    @Override
    public Connection request(Socket s, Command com) throws IOException {
        Request c = (Request) super.request(s, com);
        return c;
    }

    public ServerInfo redirect() {
        // Find which server should be connected to.
        // TODO

        return null;
    }
}
