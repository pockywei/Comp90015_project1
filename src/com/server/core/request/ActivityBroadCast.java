package com.server.core.request;

import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.inter.ConnectionListener;

public class ActivityBroadCast extends AbstractRequest {

    public ActivityBroadCast(ConnectionListener connection) {
        super(connection);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Message getRequestMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
