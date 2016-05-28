package com.server.core.listener;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;

import com.protocal.connection.inter.SocketListener;

public class SSLServerListener extends AbstractServerListener {

    private SSLServerSocket sslServerSocket;

    public SSLServerListener(SocketListener listener, int localPort)
            throws IOException {
        super(listener, localPort);
        
        start();
    }

    @Override
    protected Socket accept() throws Exception {
        return null;
    }

    @Override
    public void stop() {
        super.stop();
        try {
            if (sslServerSocket != null) {
                sslServerSocket.close();
                log.debug("SSLServerSocket has been safely closed.");
            }
        }
        catch (IOException e) {
            log.error("can not close SSLServerSocket.");
        }
    }

}
