package com.protocal.conn;

import java.io.IOException;
import java.net.Socket;

import com.protocal.connection.inter.ConnectionListener;
import com.utils.log.Log;

public class Connection implements ConnectionListener {
    private static final Log log = Log.getInstance();
    private Socket socket = null;
    private Writer writer = null;
    private Reader reader = null;
    protected ConnectionType type = null;

    public Connection(Socket socket) throws Exception {
        this.socket = socket;
        this.reader = new Reader(socket, this);
        this.writer = new Writer(socket, this);
    }

    public boolean isClosed() {
        if (socket == null) {
            return false;
        }
        return socket.isClosed();
    }

    public ConnectionType getType() {
        return type;
    }

    @Override
    public void close() {
        try {
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
            
        }
    }
}
