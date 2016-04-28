package com.protocal.conn.inter;

import java.io.IOException;
import java.net.Socket;

public interface SocketListener {

    public void distributSocket(Socket s) throws IOException;

}
