package com.protocal.connection;

import java.io.IOException;
import java.net.Socket;

import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.ConnectionType;
import com.protocal.connection.inter.Response;
import com.utils.UtilHelper;
import com.utils.log.Log;

public class Connection implements ConnectionListener {
    private static final Log log = Log.getInstance();
    private Socket socket = null;
    private WriteTask writer = null;
    private ReadTask reader = null;
    protected ConnectionType type = null;

    public Connection(Socket socket, Response response) throws Exception {
        this.socket = socket;
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

    @Override
    public void close() {
        try {
            // delete from Server connection list.
            // TODO
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

    @Override
    public void setConnectionType(ConnectionType type) {
        if (this.type == null) {
            this.type = type;
            // add the connection to the ServerImpl by different type.
            // TODO

        }
    }

    @Override
    public boolean sendMessage(String msg) {
        if (isClosed() || UtilHelper.isEmptyStr(msg) || writer == null) {
            return false;
        }
        writer.sendMessage(msg);
        return true;
    }
}
