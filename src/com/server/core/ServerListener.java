package com.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.base.BaseRunnable;
import com.protocal.conn.inter.SocketListener;
import com.server.ServerSettings;
import com.utils.log.exception.ListeningException;

public class ServerListener extends BaseRunnable {

    private ServerSocket serverSocket = null;
    private int localPort;
    private SocketListener listener;

    public ServerListener(SocketListener listener) throws IOException {
        localPort = ServerSettings.getLocalPort();
        serverSocket = new ServerSocket(localPort);
        this.listener = listener;
        start();
    }

    @Override
    public boolean runTask() throws Exception {
        log.info("listening for new connections on " + localPort);
        try {
            while (!stop) {
                Socket incomeSocket = serverSocket.accept();
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

    @Override
    public void stop() {
        super.stop();
        listener = null;
        try {
            if (serverSocket != null) {
                serverSocket.close();
                log.debug("serverSocket has been safely closed.");
            }
        }
        catch (IOException e) {
            log.error("can not close serversocket.");
        }
    }
}
