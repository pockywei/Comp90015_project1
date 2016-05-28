package com.protocal.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.beans.Record;
import com.beans.UserInfo;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.ConnectionType;
import com.protocal.connection.inter.Response;
import com.server.core.listener.LockState;
import com.server.core.response.ServerResponse;
import com.utils.UtilHelper;
import com.utils.log.Log;

public final class Connection {
    private static final Log log = Log.getInstance();
    private Socket socket = null;
    private WriteTask writer = null;
    private ReadTask reader = null;
    private ConnectionType type = null;
    private ConnectionListener listener = null;
    private Record connectionInfo = null;
    // recording each user's lock request.
    private ConcurrentHashMap<UserInfo, LockState[]> serverWaitMap = new ConcurrentHashMap<>();
    private LockState[] clientWaitStates = new LockState[0];

    public void setWaitState(int waitCount, UserInfo register) {
        LockState[] initStates = LockState.initLockStateList(waitCount);
        if (connectionInfo instanceof UserInfo) {
            synchronized (clientWaitStates) {
                clientWaitStates = initStates;
                return;
            }
        }
        serverWaitMap.put(register, initStates);
    }

    public int reduceCount(boolean isAllow, UserInfo register) {
        if (connectionInfo instanceof UserInfo) {
            synchronized (clientWaitStates) {
                changeStates(clientWaitStates, isAllow);
                return countWaiting(clientWaitStates);
            }
        }
        synchronized (serverWaitMap) {
            LockState[] states = serverWaitMap.get(register);
            if (states == null) {
                return 0;
            }
            changeStates(states, isAllow);
            return countWaiting(serverWaitMap.get(register));
        }
    }

    public LockState hasFinishLock(UserInfo register) {
        LockState state = null;
        if (connectionInfo instanceof UserInfo) {
            return isFinish(clientWaitStates);
        }
        final LockState[] states = serverWaitMap.get(register);
        if (states == null) {
            return null;
        }
        state = isFinish(states);
        // wait process finish, remove the waiting record
        if (state != LockState.WAITTED) {
            serverWaitMap.remove(register);
        }
        return state;
    }

    private LockState isFinish(final LockState[] states) {
        boolean isAllow = true;
        for (LockState s : states) {
            if (s == LockState.WAITTED) {
                return LockState.WAITTED;
            }
            if (s == LockState.DENIED) {
                isAllow = false;
            }
        }
        return isAllow ? LockState.ALLOWED : LockState.DENIED;
    }

    private void changeStates(LockState[] states, boolean isAllow) {
        for (int i = 0; i < states.length; i++) {
            if (states[i] == LockState.WAITTED) {
                states[i] = isAllow ? LockState.ALLOWED : LockState.DENIED;
                break;
            }
        }
    }

    private int countWaiting(final LockState[] states) {
        int waitCount = 0;
        for (LockState s : states) {
            if (s == LockState.WAITTED)
                waitCount++;
        }
        return waitCount;
    }

    public Connection(Socket socket, ConnectionListener listener)
            throws Exception {
        this(socket, null, listener);
    }

    public Connection(Socket socket, Response response) throws Exception {
        this(socket, response, null);
    }

    public Connection(Socket socket, Response response,
            ConnectionListener listener) throws Exception {
        this.socket = socket;
        this.listener = listener;
        connect(response);
    }

    /**
     * Start to connect
     * 
     * @param response
     * @param listener
     * @throws Exception
     */
    public void connect(Response response) throws Exception {
        if (response == null) {
            return;
        }
        if (this.reader != null) {
            this.reader.setResponse(response);
        }
        else {
            // start 2 tasks for reading and writing
            this.reader = new ReadTask(socket, response, this);
            this.writer = new WriteTask(socket, this);
            log.info("connection has been linked to the socket: "
                    + getSocketAddr());
        }
    }

    public boolean isClosed() {
        if (socket == null) {
            return true;
        }
        return socket.isClosed();
    }

    public ConnectionType getType() {
        return type;
    }

    public String getSocketAddr() {
        return UtilHelper.getSocketAddr(socket);
    }

    public void close() {
        try {
            // delete from Server connection list.
            if (listener != null) {
                listener.closeConnection(this);
                listener = null;
            }
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        catch (Exception e) {
            log.error("connection writer or reader close exception. " + e);
        }
        finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            }
            catch (IOException e) {
                log.error("socket close exception. " + e);
            }
        }
    }

    /**
     * Classify different connection type after the authentication for both
     * server and client.
     * 
     * @param type
     */
    public void classifyType(ConnectionType type, Record connectionInfo)
            throws Exception {
        if (this.type == null) {
            this.type = type;
            setConnectionInfo(connectionInfo);
            // the connection has been authenticated, change the response
            connect(new ServerResponse());
            // add the connection to the ServerImpl by different type.
            if (listener != null) {
                listener.addConnection(this);
            }
        }
    }

    public final Record getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(Record connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    /**
     * Send message
     * 
     * @param com
     * @param activity
     * @return true: send success; false: send failed as connection has been
     *         closed.
     */
    public boolean sendMessage(String msg) {
        if (isClosed() || UtilHelper.isEmptyStr(msg) || writer == null) {
            return false;
        }
        writer.sendMessage(msg);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Connection c = (Connection) o;
        return getSocketAddr().equals(c.getSocketAddr());
    }
}
