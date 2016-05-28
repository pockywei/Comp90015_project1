package com.server.core.response;

import com.protocal.Message;
import com.protocal.connection.Connection;

public class SecureResponse extends AbstractResponse {

    @Override
    public boolean process(Message msg, Connection connection)
            throws Exception {

        switch (msg.getCommand()) {
            case AUTHENTICATE:
                return new AuthenticateResponse(msg.toServerInfo()).process(msg,
                        connection);
            case LOGIN:
                return new LoginResponse(msg.toUserInfo()).process(msg,
                        connection);
            case REGISTER:
                return new RegisterResponse(msg.toUserInfo()).process(msg,
                        connection);
            default:
                break;
        }
        // no cases matched, close connection.
        return false;
    }

}
