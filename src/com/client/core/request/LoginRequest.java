package com.client.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.inter.ConnectionListener;

public class LoginRequest extends AbstractRequest {

    private String username;
    private String secret;

    public LoginRequest(ConnectionListener connection, String username,
            String secret) {
        super(connection);
        this.username = username;
        this.secret = secret;
    }

    @Override
    public Message getRequestMessage() {
        return Message.getUserMsg(username, secret, Command.LOGIN);
    }
}
