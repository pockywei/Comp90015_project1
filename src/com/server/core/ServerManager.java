package com.server.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.protocal.connection.ssl.SocketFactory;
import com.server.ServerSettings;
import com.server.core.database.LocalStorage;
import com.server.core.request.ActivityBroadCast;
import com.server.core.request.AnnounceRequest;
import com.server.core.request.AuthenticateRequest;
import com.server.core.request.LockRequest;
import com.server.core.request.RedirectRequest;
import com.server.core.request.SecretRequest;
import com.server.core.response.ServerResponse;
import com.utils.log.CrashHandler;

public class ServerManager extends AbstractServer {

    private static ServerManager instance = null;
    private List<Connection> clientCache = new ArrayList<>();
    private ConcurrentHashMap<String, Connection> serverCache = new ConcurrentHashMap<>();

    public synchronized static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    public void setRootConnection(Connection root, String user) {
        serverCache.put(user, root);
    }

    public Connection getRootConnection(String user) {
        return serverCache.get(user);
    }

    public void removeRootConnection(String user) {
        serverCache.remove(user);
    }

    public Connection createConnection(Socket s) throws Exception {
        return createConnection(s, false);
    }

    public Connection createConnection(Socket s, boolean isRoot)
            throws Exception {
        return new Connection(s, new ServerResponse(), this, isRoot);
    }

    @Override
    public void sendAuthenticate() throws Exception {
        // no remote server info. start as a root server.
        if (ServerSettings.isRootServer()) {
            log.info(
                    "the current server will start alone as a root server, here by the secret: "
                            + ServerSettings.getLocalSecret());
        }
        else {
            log.info("sending an authenticate to remote server");
            log.info("the current server's secret: "
                    + ServerSettings.getLocalSecret());

            Connection c = createConnection(SocketFactory.getServerSocket(),
                    true);
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
    public ServerInfo redirectClient() {
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
     * Top root server will pick one of the adjacent servers as a new top root
     * server.
     * 
     * @return
     */
    public ServerInfo redirectServer() {
        List<Connection> connections = getAuthenticatedServers();
        ServerInfo server = null;
        synchronized (connections) {
            for (Connection c : connections) {
                // only ssl server can be the reauthenticated target
                if (c.isSSLConnection()) {
                    server = (ServerInfo) c.getConnectionInfo();
                    if (Protocal.isRedirect(ServerSettings.getLocalLoad(),
                            server.getLoad())) {
                        return server;
                    }
                }
            }
        }
        return server;
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
        synchronized (clientCache) {
            clientCache.remove(c);
        }
    }

    public void registerFailed(Connection c, String responseMsg) {
        c.sendMessage(responseMsg);
        synchronized (clientCache) {
            clientCache.remove(c);
        }
    }

    public Connection getRegisterConnection(UserInfo user) {
        synchronized (clientCache) {
            for (Connection c : clientCache) {
                if (user.getKey().equals(c.getConnectionInfo().getKey())) {
                    return c;
                }
            }
        }
        return null;
    }

    public void addRegisterConnection(Connection registerCon) {
        synchronized (clientCache) {
            clientCache.add(registerCon);
        }
    }

    @Override
    public void crashBroadcast() {
        // the current server will crash and it will process all logged clients
        // and all adjacent servers to redirect to another server
        log.info(
                "the server will start to send a broadcase for gracefully quit");
        if (!ServerSettings.isRootServer()) {
            // the server will redirect all connection to its root server
            ServerInfo remoteRoot = new ServerInfo()
                    .setHostname(ServerSettings.getRemoteHost())
                    .setPort(ServerSettings.getRemotePort())
                    .setSecret(ServerSettings.getRemoteSecret());
            // close the root connection
            List<Connection> servers = getAuthenticatedServers();
            synchronized (servers) {
                for (Connection c : servers) {
                    if (c.isRoot()) {
                        // close the connection with the new root server to make
                        // sure that the new root will not redirect request to
                        // the current server.
                        crashNotice(remoteRoot, c);
                        break;
                    }
                }
            }
            return;
        }

        // the current crashed server is the top root server
        ServerInfo redirectInfo = redirectServer();
        if (redirectInfo == null) {
            // TODO compatibility issue, the redirect process can not be able to
            // do the broadcost message.
            return;
        }
        List<Connection> servers = getAuthenticatedServers();
        Connection redirect = null;
        synchronized (servers) {
            for (Connection c : servers) {
                if (redirectInfo.getKey()
                        .equals(c.getConnectionInfo().getKey())) {
                    redirect = c;
                    break;
                }
            }
        }
        synchronized (redirect) {
            if (redirect != null) {
                // request the target server's secret
                new SecretRequest(redirect).request();
            }
        }
    }

    public void crashNotice(final ServerInfo s, final Connection from) {
        // redirect all client to the new root server
        log.debug("redirect all client to the new root server: "
                + s.getHostname() + ":" + s.getPort());
        List<Connection> clients = getLoggedUserList();
        synchronized (clients) {
            for (Connection c : clients) {
                new RedirectRequest(c, s, Command.REDIRECT).request();
            }
        }

        log.debug("redirect other server to the new root server: "
                + s.getHostname() + ":" + s.getPort());
        // reauthenticate all other servers to the new root server
        List<Connection> servers = getAuthenticatedServers();
        synchronized (servers) {
            for (Connection c : servers) {
                if (!c.equals(from)) {
                    new RedirectRequest(c, s, Command.REAUTHENTICATE).request();
                }
            }
        }

        // System exit
        CrashHandler.getInstance().errorExit();
    }
}
