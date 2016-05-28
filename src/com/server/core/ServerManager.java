package com.server.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.server.ServerSettings;
import com.server.core.database.LocalStorage;
import com.server.core.request.ActivityBroadCast;
import com.server.core.request.AnnounceRequest;
import com.server.core.request.AuthenticateRequest;
import com.server.core.request.LockRequest;
import com.server.core.response.ServerResponse;
import com.utils.UtilHelper;

public class ServerManager extends AbstractServer {

    private static ServerManager instance = null;
    private List<Connection> registerClientList = new ArrayList<>();
    private ConcurrentHashMap<String, Connection> rootMap = new ConcurrentHashMap<>();

    public void setRootConnection(Connection root, String user) {
        rootMap.put(user, root);
    }

    public Connection getRootConnection(String user) {
        return rootMap.get(user);
    }

    public void removeRootConnection(String user) {
        rootMap.remove(user);
    }

    public synchronized static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    private Connection createConnection(Socket s) throws Exception {
        return new Connection(s, new ServerResponse(), this);
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
            log.info("the current server's secret: "
                    + ServerSettings.getLocalSecret());
            Connection c = createConnection(
                    new Socket(ServerSettings.getRemoteHost(),
                            ServerSettings.getRemotePort()));
            // create a new connection to remote server.
            if (new AuthenticateRequest(c).request()) {
                c.classifyType(ConnectionType.SERVER_CONN,
                        new ServerInfo()
                                .setHostname(ServerSettings.getRemoteHost())
                                .setPort(ServerSettings.getRemotePort()));
            }
        }
    }

    @Override
    public ServerInfo redirect() {
        // Find which server should be connected to.
        List<Connection> connections = getAuthenticatedServers();
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
    public int sendAnnounce(final Connection from) {
        List<Connection> connections = getAuthenticatedServers();
        int count = 0;
        synchronized (connections) {
            for (Connection c : connections) {
                // server will not send the message back.
                if (!c.equals(from)) {
                    new AnnounceRequest(c).request();
                    count++;
                }
            }
            return count;
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
        List<Connection> connections = getAuthenticatedServers();
        int count = 0;
        synchronized (connections) {
            for (Connection c : connections) {
                if (!c.equals(from)) {
                    // server will not send the message back.
                    new LockRequest(c, username, secret).request();
                    count++;
                }
            }
            return count;
        }
    }

    /**
     * No reply
     * 
     * @param from
     * @param username
     * @param message
     */
    @Override
    public int sendActivityBroadcast(final Connection from,
            final String username, final String message) {
        List<Connection> serverConnections = getAuthenticatedServers();
        int count = 0;
        synchronized (serverConnections) {
            for (Connection c : serverConnections) {
                if (!c.equals(from)) {
                    // server will not send the message back.
                    new ActivityBroadCast(c, username, message).request();
                    count++;
                }
            }
        }
        List<Connection> userConnections = getLoggedUserList();
        synchronized (userConnections) {
            for (Connection c : userConnections) {
                new ActivityBroadCast(c, username, message).request();
                count++;
            }
        }
        return count;
    }

    public void registerSuccess(Connection c, UserInfo user,
            String responseMsg) {
        LocalStorage.getInstance().addUser(user);
        c.sendMessage(responseMsg);
        synchronized (registerClientList) {
            registerClientList.remove(c);
        }
    }

    public void registerFailed(Connection c, String responseMsg) {
        c.sendMessage(responseMsg);
        synchronized (registerClientList) {
            registerClientList.remove(c);
        }
    }

    public Connection getRegisterConnection(UserInfo user) {
        synchronized (registerClientList) {
            for (Connection c : registerClientList) {
                if (user.getKey().equals(c.getConnectionInfo().getKey())) {
                    return c;
                }
            }
        }
        return null;
    }

    public void addRegisterConnection(Connection registerCon) {
        synchronized (registerClientList) {
            registerClientList.add(registerCon);
        }
    }
}
