package com.server.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.server.ServerSettings;
import com.server.core.request.ActivityBroadCast;
import com.server.core.request.AnnounceRequest;
import com.server.core.request.AuthenticateRequest;
import com.server.core.request.LockRequest;
import com.utils.UtilHelper;

public class ServerManager extends AbstractServer {

    private static ServerManager instance = null;
    private List<Connection> registerList = new ArrayList<>();

    public synchronized static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    @Override
    public void sendAuthenticate() throws Exception {
        // no remote server info. start as a root server.
        if (UtilHelper.isEmptyStr(ServerSettings.getRemoteHost())
                || ServerSettings.getRemotePort() == 0
                || UtilHelper.isEmptyStr(ServerSettings.getRemoteSecret())) {
            log.info("the current server will start alone, here by the secret: "
                    + ServerSettings.getLocalSecret());
        }
        else {
            log.info("sending an authenticate to remote server");
            Connection c = distributSocket(
                    new Socket(ServerSettings.getRemoteHost(),
                            ServerSettings.getRemotePort()));
            // create a new connection to remote server.
            if (new AuthenticateRequest(c).request()) {
                c.classifyConnectionType(ConnectionType.SERVER_CONN,
                        new ServerInfo()
                                .setHostname(ServerSettings.getRemoteHost())
                                .setPort(ServerSettings.getRemotePort()));
            }
        }
    }

    @Override
    public ServerInfo redirect() {
        // Find which server should be connected to.
        List<Connection> connections = getAuthentiedServers();
        synchronized (connections) {
            for (Connection c : connections) {
                ServerInfo serverInfo = (ServerInfo) c.getConnectionInfo();
                if (Protocal.isRedirect(ServerSettings.getLocalLoad(),
                        serverInfo.getLoad())) {
                    return serverInfo;
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
    public void sendAnnounce(final Connection from) {
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
    @Override
    public int sendLockRequest(final Connection from, final String username,
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
        return connections.size();
    }

    /**
     * No reply
     * 
     * @param from
     * @param username
     * @param message
     */
    @Override
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

    /**
     * true: the user is logged false: the user should be authenticated.
     * 
     * @param user
     * @return
     */
    public boolean isUserLogged(final UserInfo user) {
        final List<Connection> loggedUsers = getLoggedUserList();
        synchronized (loggedUsers) {
            for (Connection c : loggedUsers) {
                if (user.getKey().equals(c.getConnectionInfo().getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * true: the server has been authenticated; false: not
     * 
     * @param server
     * @return
     */
    public boolean hasServer(final ServerInfo server) {
        final List<Connection> servers = getAuthentiedServers();
        synchronized (server) {
            for (Connection c : servers) {
                if (server.getKey().equals(c.getConnectionInfo().getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Connection getRegisterConnection(UserInfo user) {
        synchronized (registerList) {
            for (Connection c : registerList) {
                if (user.getKey().equals(c.getConnectionInfo().getKey())) {
                    return c;
                }
            }
        }
        return null;
    }

    public void addRegisterConnection(Connection registerCon) {
        synchronized (registerList) {
            registerList.add(registerCon);
        }
    }

    public void removeRegisterConnection(Connection c) {
        synchronized (registerList) {
            registerList.remove(c);
        }
    }
}
