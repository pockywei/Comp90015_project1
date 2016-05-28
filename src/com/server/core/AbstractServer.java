package com.server.core;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.base.BaseManager;
import com.beans.ServerInfo;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.SocketListener;
import com.server.ServerSettings;
import com.server.core.database.LocalStorage;
import com.server.core.listener.ServerListener;
import com.server.core.response.SecureResponse;
import com.utils.UtilHelper;
import com.utils.log.CrashHandler;

public abstract class AbstractServer extends BaseManager
        implements SocketListener, ConnectionListener {

    private List<Connection> userConnections = new ArrayList<>();
    private List<Connection> serverConnections = new ArrayList<>();
    private ServerListener listener = null;

    public AbstractServer() {
        super();
        try {
            listener = new ServerListener(this, ServerSettings.getLocalPort());
        }
        catch (IOException e) {
            log.fatal("failed to startup a listening thread: " + e);
            CrashHandler.getInstance().errorExit();
        }
        initServer();
        start();
    }

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

    public abstract int sendAnnounce(Connection from);

    public abstract int sendLockRequest(final Connection from,
            final String username, final String secret);

    public abstract ServerInfo redirect();
    
    public abstract void sendAuthenticate() throws Exception;

    public abstract int sendActivityBroadcast(final Connection from,
            final String username, final String message);

    @Override
    public boolean runTask() throws Exception {
        log.info("using activity interval of "
                + ServerSettings.getActivityInterval() + " milliseconds");
        while (!stop) {
            // do something with 5 second intervals in between
            try {
                Thread.sleep(ServerSettings.getActivityInterval());
            }
            catch (InterruptedException e) {
                log.info("received an interrupt, system is shutting down");
                break;
            }
            if (!stop) {
                log.info("server doing Announce");
                sendAnnounce(null);
            }
        }
        log.info("closing user connections: " + userConnections.size()
                + " server connections: " + serverConnections.size());
        CrashHandler.getInstance().exit();
        return false;
    }

    @Override
    public void distributSocket(Socket s) throws Exception {
        log.debug("incomming socket: " + UtilHelper.getSocketAddr(s));
        new Connection(s, new SecureResponse(), this);
    }

    @Override
    public void closeConnection(Connection c) throws Exception {
        if (c == null || c.getType() == null) {
            return;
        }
        switch (c.getType()) {
            case USER_CONN:
                synchronized (userConnections) {
                    userConnections.remove(c);
                    // update server user load.
                    ServerSettings.setLocalLoad(userConnections.size());
                }
                break;
            case SERVER_CONN:
                // remove connection.
                synchronized (serverConnections) {
                    serverConnections.remove(c);
                }
                break;
        }
    }

    /**
     * The added connection has been authenticated.
     * 
     */
    @Override
    public void addConnection(Connection c) {
        switch (c.getType()) {
            case USER_CONN:
                synchronized (userConnections) {
                    userConnections.add(c);
                    // add server user load.
                    ServerSettings.setLocalLoad(userConnections.size());
                }
                break;
            case SERVER_CONN:
                synchronized (serverConnections) {
                    serverConnections.add(c);
                }
                break;
        }
    }

    public void updateServerInfo(final ServerInfo update, Connection c) {
        synchronized (c) {
            c.setConnectionInfo(update);
        }
    }

    public final List<Connection> getLoggedUserList() {
        return userConnections;
    }

    public final List<Connection> getAuthenticatedServers() {
        return serverConnections;
    }

    @Override
    public void clear() {
        crashBroadcast();
        stop();
        if (listener != null) {
            listener.stop();
        }
        LocalStorage.getInstance().clear();
        synchronized (userConnections) {
            for (Connection c : userConnections) {
                c.close();
            }
            userConnections.clear();
        }
        synchronized (serverConnections) {
            for (Connection c : serverConnections) {
                c.close();
            }
            serverConnections.clear();
        }
    }

    /**
     * Once the server is stopped by any exceptions, it will broadcast redirect
     * messages to the clients and servers
     * 
     */
    private void crashBroadcast() {

    }
}
