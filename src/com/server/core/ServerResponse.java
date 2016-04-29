package com.server.core;

import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.Response;

public class ServerResponse implements Response{

    @Override
    public boolean process(String json, ConnectionListener connection)
            throws Exception {
        return false;
    }

}
