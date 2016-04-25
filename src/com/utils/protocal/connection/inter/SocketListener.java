package com.utils.protocal.connection.inter;

import java.io.IOException;
import java.net.Socket;

import com.utils.protocal.connection.Response;

public interface SocketListener extends RequestListener {

    public Response response(Socket s) throws IOException;

}
