package com.client.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.inter.ConnectionListener;

public class LogoutRequest extends AbstractRequest {

    public LogoutRequest(ConnectionListener connection) {
        super(connection);
    }

    @Override
    public Message getRequestMessage() {
        return new Message(Command.LOGOUT);
    }

}
