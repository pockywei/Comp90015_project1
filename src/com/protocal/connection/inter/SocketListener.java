package com.protocal.connection.inter;

import java.io.IOException;
import java.net.Socket;

import com.protocal.connection.Connection;

public interface SocketListener extends RequestListener {

    public Connection response(Socket s) throws IOException;

}
