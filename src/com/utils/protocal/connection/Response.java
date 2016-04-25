package com.utils.protocal.connection;

import java.io.IOException;
import java.net.Socket;

import com.utils.protocal.json.ParserJson;

public class Response extends Connection{
    
    public Response(Socket socket) throws IOException {
        super(socket);
    }


    @Override
    public void runTask() throws Exception {
        read();
    }


    @Override
    protected boolean process(String json) throws Exception {
        return false;
    }

}
