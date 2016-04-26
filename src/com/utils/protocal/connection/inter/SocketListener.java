package com.utils.protocal.connection.inter;

import java.io.IOException;
import java.net.Socket;

import com.utils.protocal.connection.Connection;

public interface SocketListener extends RequestListener {

    public Connection response(Socket s) throws IOException;

}
