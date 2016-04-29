package com.server.core.request;

import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.inter.ConnectionListener;

public class LockRequest extends AbstractRequest {

    public LockRequest(ConnectionListener connection) {
        super(connection);
    }

    @Override
    public Message getRequestMessage() {
        return null;
    }

}
