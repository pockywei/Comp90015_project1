package com.protocal.connection.inter;

import java.io.IOException;
import java.net.Socket;

import com.protocal.Command;
import com.protocal.connection.Connection;

public interface RequestListener {

    public Connection request(Socket s, Command com) throws IOException;

}
