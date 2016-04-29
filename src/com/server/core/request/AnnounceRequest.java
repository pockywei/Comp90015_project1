package com.server.core.request;

import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;

public class AnnounceRequest extends AbstractRequest{

    public AnnounceRequest(Connection connection) {
        super(connection);
    }

    @Override
    public Message getRequestMessage() {
        return null;
    }


}
