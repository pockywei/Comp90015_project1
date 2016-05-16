package com.protocal.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.base.BaseRunnable;
import com.utils.UtilHelper;

public abstract class AbstractSocketTask extends BaseRunnable {

    private Socket socket;
    protected Connection connection = null;

    public AbstractSocketTask(Socket socket,
            Connection connection) throws Exception {
        this.socket = socket;
        this.connection = connection;
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
        connection = null;
    }
}
