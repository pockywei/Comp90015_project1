package com.protocal.connection;

import java.io.IOException;
import java.net.Socket;

import com.beans.Record;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.ConnectionType;
import com.protocal.connection.inter.Response;
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
    public volatile int waitCount = 0;

    public Connection(Socket socket, Response response) throws Exception {
        this(socket, response, null);
    }

    public Connection(Socket socket, Response response,
            ConnectionListener listener) throws Exception {
        this.socket = socket;
        this.listener = listener;
        this.reader = new ReadTask(socket, response, this);
        this.writer = new WriteTask(socket, this);
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
    public void classifyConnectionType(ConnectionType type,
            Record connectionInfo) {
        if (this.type == null) {
            this.type = type;
            setConnectionInfo(connectionInfo);
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
        if (o == null || connectionInfo == null) {
            return false;
        }
        Connection c = (Connection) o;
        return connectionInfo.getKey().equals(c.getConnectionInfo().getKey());
    }
}
