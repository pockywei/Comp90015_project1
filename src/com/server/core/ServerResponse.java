package com.server.core;

import com.protocal.connection.Connection;
import com.protocal.connection.inter.Response;

public class ServerResponse implements Response{

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        return false;
    }

}
