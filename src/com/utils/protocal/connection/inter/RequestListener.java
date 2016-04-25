package com.utils.protocal.connection.inter;

import java.io.IOException;
import java.net.Socket;

import com.utils.protocal.Command;
import com.utils.protocal.connection.Request;

public interface RequestListener {

    public Request request(Socket s, Command com) throws IOException;

}
