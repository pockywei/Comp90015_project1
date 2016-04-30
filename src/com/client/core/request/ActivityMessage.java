package com.client.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;

public class ActivityMessage extends AbstractRequest {

    private String username;
    private String secret;
    private String message;

    public ActivityMessage(Connection connection, String username,
            String secret, String message) {
        super(connection);
        this.username = username;
        this.secret = secret;
        this.message = message;
    }

    @Override
    public Message getRequestMessage() {
        return Message.getActivityMsg(username, secret,
                Message.getActivity(message), Command.ACTIVITY_MESSAGE);
    }

}
