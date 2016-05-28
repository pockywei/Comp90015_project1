package com.protocal.connection.ssl;

import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

import com.client.UserSettings;
import com.server.ServerSettings;
import com.utils.log.Log;

public class SocketFactory {

    private static final Log log = Log.getInstance();
    private static SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory
            .getDefault();

    public static Socket getClientSocket() throws Exception {
        return createSocket(UserSettings.getRemoteHost(),
                UserSettings.getSSLRemotePort(), UserSettings.getRemotePort());
    }

    public static Socket getServerSocket() throws Exception {
        return createSocket(ServerSettings.getRemoteHost(),
                ServerSettings.getSSLRemotePort(),
                ServerSettings.getRemotePort());
    }

    private static Socket createSocket(String host, int sslport, int port)
            throws Exception {
        try {
            Socket sslSocket = sslsocketfactory.createSocket(host, sslport);
            return sslSocket;
        }
        catch (Exception e) {
            log.debug("[SSL Socket connection attempt is failed by exception: "
                    + e + "], auto switich to normal Socket to connect");
            return new Socket(host, port);
        }
    }
}
