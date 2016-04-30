package com.protocal.connection.inter;

import java.net.Socket;

import com.protocal.connection.Connection;

public interface SocketListener {
    
    public Connection distributSocket(Socket s) throws Exception;
    
}
