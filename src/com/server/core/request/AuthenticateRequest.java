package com.server.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;
import com.server.ServerSettings;

public class AuthenticateRequest extends AbstractRequest {

    public AuthenticateRequest(Connection connection) {
        super(connection);
    }

    @Override
    public Message getRequestMessage() {
        return Message.getAuthenticateMsg(ServerSettings.getRemoteSecret(),
                Command.AUTHENTICATE);
    }

}
