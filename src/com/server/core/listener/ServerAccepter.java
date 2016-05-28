package com.server.core.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.protocal.connection.inter.SocketListener;

public class ServerAccepter extends AbstractAccepter {

    private ServerSocket serverSocket = null;

    public ServerAccepter(SocketListener listener, int localPort)
            throws IOException {
        super(listener, localPort);
        serverSocket = new ServerSocket(localPort);
        start();
    }
    
    @Override
    protected Socket accept() throws Exception{
        return serverSocket.accept();
    }

    @Override
    public void stop() {
        super.stop();
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
