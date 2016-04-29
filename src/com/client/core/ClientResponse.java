package com.client.core;

import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.Response;

public class ClientResponse implements Response{

    @Override
    public boolean process(String json, ConnectionListener connection)
            throws Exception {
        return false;
    }

}
