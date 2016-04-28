package com.protocal.conn;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.base.BaseRunnable;
import com.protocal.connection.inter.ConnectionListener;
import com.utils.UtilHelper;

public abstract class AbstractSocketTask extends BaseRunnable {

    private Socket socket;
    protected ConnectionListener connectionListener = null;

    public AbstractSocketTask(Socket socket,
            ConnectionListener connectionListener) throws Exception {
        this.socket = socket;
        this.connectionListener = connectionListener;
    }

    protected OutputStream getOutputStream() throws Exception {
        return socket.getOutputStream();
    }

    protected InputStream getInputStream() throws Exception {
        return socket.getInputStream();
    }

    public String getSocketAddr() {
        if (socket == null) {
            return null;
        }
        return UtilHelper.getSocketAddr(socket);
    }

    public void close() throws Exception {
        connectionListener = null;
    }
}
