package com.server.core;

import java.net.Socket;
import java.util.Collections;
import java.util.List;

import com.beans.ServerInfo;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.server.ServerSettings;
import com.server.core.request.ActivityBroadCast;
import com.server.core.request.AnnounceRequest;
import com.server.core.request.AuthenticateRequest;
import com.server.core.request.LockRequest;
import com.utils.UtilHelper;
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
            sendAuthenticate();
        }
        catch (Exception e) {
            log.error("failed to make connection to "
                    + ServerSettings.getRemoteHost() + ":"
                    + ServerSettings.getRemotePort() + " :" + e);
            CrashHandler.getInstance().errorExit();
        }
    }

    public ServerInfo redirect() {
        // Find which server should be connected to.
        List<ServerInfo> serverInfos = LocalStorage.getInstance()
                .getAdjacentServers();
        synchronized (serverInfos) {
            // Ascending order.
            Collections.sort(serverInfos);
            for (ServerInfo s : serverInfos) {
                if (Protocal.isRedirect(ServerSettings.getLocalLoad(),
                        s.getLoad())) {
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * No reply
     * 
     */
    @Override
    public void serverAnnounce(final Connection from) {
        log.info("broadcast a serverAnnounce to adjacent servers");
        List<Connection> connections = getAuthentiedServers();
        synchronized (connections) {
            for (Connection c : connections) {
                // server will not send the message back.
                if (!c.equals(from)) {
                    new AnnounceRequest(c).request();
                }
            }
        }
    }

    /**
     * Wait for reply
     * 
     * @param from
     * @param username
     * @param secret
     */
    public void sendLockRequest(final Connection from, final String username,
            final String secret) {
        log.info("broadcast a LockRequest to adjacent servers");
        List<Connection> connections = getAuthentiedServers();
        synchronized (connections) {
            for (Connection c : connections) {
                if (!c.equals(from)) {
                    // server will not send the message back.
                    new LockRequest(c, username, secret).request();
                }
            }
        }
    }

    /**
     * No reply
     * 
     * @param from
     * @param username
     * @param message
     */
    public void sendActivityBroadcast(final Connection from,
            final String username, final String message) {
        log.info(
                "broadcast an activity message to adjacent servers and clients");
        List<Connection> serverConnections = getAuthentiedServers();
        synchronized (serverConnections) {
            for (Connection c : serverConnections) {
                if (!c.equals(from)) {
                    // server will not send the message back.
                    new ActivityBroadCast(c, username, message).request();
                }
            }
        }
        List<Connection> userConnections = getLoggedUserList();
        synchronized (userConnections) {
            for (Connection c : userConnections) {
                new ActivityBroadCast(c, username, message).request();
            }
        }
    }

    public void sendAuthenticate() throws Exception {
        // no remote server info. start as a root server.
        if (UtilHelper.isEmptyStr(ServerSettings.getRemoteHost())
                || ServerSettings.getRemotePort() == 0) {
            log.info("no remote server info, starting as a root server");
        }
        else {
            log.info("sending an authenticate to remote server");
            Connection c = distributSocket(
                    new Socket(ServerSettings.getRemoteHost(),
                            ServerSettings.getRemotePort()));
            new AuthenticateRequest(c).request();
        }
    }
}
