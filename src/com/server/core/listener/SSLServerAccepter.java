package com.server.core.listener;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import com.protocal.connection.inter.SocketListener;

public class SSLServerAccepter extends AbstractAccepter {

    private SSLServerSocket sslServerSocket;

    public SSLServerAccepter(SocketListener listener, int localPort)
            throws IOException {
        super(listener, localPort);
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory
                .getDefault();
        sslServerSocket = (SSLServerSocket) factory
                .createServerSocket(localPort);
        start();
    }

    @Override
    protected Socket accept() throws Exception {
        return sslServerSocket.accept();
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
