package com.server.core.request;

import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;

public class LockRequest extends AbstractRequest {

    public LockRequest(Connection connection) {
        super(connection);
    }

    @Override
    public Message getRequestMessage() {
        return null;
    }

}
