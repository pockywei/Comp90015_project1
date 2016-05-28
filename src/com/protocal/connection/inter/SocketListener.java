package com.protocal.connection.inter;

import java.net.Socket;

public interface SocketListener {
    
    public void distributSocket(Socket s) throws Exception;
    
}
