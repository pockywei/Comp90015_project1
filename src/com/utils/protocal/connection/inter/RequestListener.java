package com.utils.protocal.connection.inter;

import java.io.IOException;
import java.net.Socket;

import com.utils.protocal.Command;
import com.utils.protocal.connection.Connection;

public interface RequestListener {

    public Connection request(Socket s, Command com) throws IOException;

}
