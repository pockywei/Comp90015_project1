package com.server.core;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.base.BaseManager;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.SocketListener;
import com.server.ServerSettings;
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
            listener = new ServerListener(this);
        }
        catch (IOException e) {
            log.fatal("failed to startup a listening thread: " + e);
            CrashHandler.getInstance().errorExit();
        }
        initServer();
        start();
    }

    public abstract void serverAnnounce();

    public abstract void initServer();

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
                serverAnnounce();
            }
        }
        log.info("closing user connections: " + userConnections.size()
                + " server connections: " + serverConnections.size());
        CrashHandler.getInstance().exit();
        return false;
    }

    @Override
    public Connection distributSocket(Socket s) throws Exception {
        log.debug("incomming connection: " + UtilHelper.getSocketAddr(s));
        Connection c = new Connection(s, new ServerResponse(), this);
        return c;
    }

    @Override
    public void close(Connection c) throws Exception {
        switch (c.getType()) {
            case USER_CONN:
                synchronized (userConnections) {
                    userConnections.remove(c);
                    // update server user load.
                    ServerSettings.setLocalLoad(userConnections.size());
                }
                break;
            case SERVER_CONN:
                synchronized (serverConnections) {
                    serverConnections.remove(c);
                }
                break;
        }
    }

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

    public final List<Connection> getLoggedUserList() {
        return userConnections;
    }

    public final List<Connection> getAuthentiedServers() {
        return serverConnections;
    }

    @Override
    public void clear() {
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
}
