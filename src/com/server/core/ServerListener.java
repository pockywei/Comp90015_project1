package com.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.base.BaseRunnable;
import com.protocal.connection.inter.SocketListener;
import com.server.ServerSettings;
import com.utils.log.ListeningException;

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
    public void runTask() throws Exception {
        log.info("listening for new connections on " + localPort);
        try {
            while (!stop) {
                Socket incomeSocket = serverSocket.accept();
                // Incoming a client socket.
                if (listener != null) {
                    listener.response(incomeSocket);
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
    }

    @Override
    public void stop() {
        super.stop();
        listener = null;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
        catch (IOException e) {
            log.error("can not close serversocket, shutting down");
        }
    }
}
