package com.server.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;

public class SecretRequest extends AbstractRequest {

    public SecretRequest(Connection connection) {
        super(connection);
    }

    @Override
    public Message getRequestMessage() {
        return new Message(Command.SECRET_REQUEST);
    }
}
