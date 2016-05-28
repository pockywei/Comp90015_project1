package com.server.core.listener;

import java.io.IOException;
import java.net.Socket;

import com.base.BaseRunnable;
import com.protocal.connection.inter.SocketListener;
import com.utils.log.exception.ListeningException;

public abstract class AbstractServerListener extends BaseRunnable {

    private int localPort;
    private SocketListener listener;

    public AbstractServerListener(SocketListener listener, int localPort)
            throws IOException {
        this.listener = listener;
        this.localPort = localPort;
    }

    @Override
    public boolean runTask() throws Exception {
        log.info("listening for new connections on " + localPort);
        try {
            while (!stop) {
                Socket incomeSocket = accept();
                // Incoming a client socket.
                if (listener != null) {
                    listener.distributSocket(incomeSocket);
                }
            }
        }
        catch (Exception e) {
            log.error("client socket accepting exception, shutting down");
            throw new ListeningException();
        }
        finally {
            stop();
        }
        return false;
    }

    protected abstract Socket accept() throws Exception;

    @Override
    public void stop() {
        super.stop();
        listener = null;
    }
}
