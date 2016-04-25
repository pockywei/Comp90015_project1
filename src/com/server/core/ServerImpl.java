package com.server.core;

import java.io.IOException;
import java.net.Socket;

import com.server.ServerSettings;
import com.utils.log.CrashHandler;
import com.utils.protocal.Command;
import com.utils.protocal.connection.Request;
import com.utils.protocal.connection.Response;

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
    public Response response(Socket s) throws IOException {
        Response c = super.response(s);
        return c;
    }

    @Override
    public Request request(Socket s, Command com) throws IOException {
        Request c = super.request(s, com);
        return c;
    }
}