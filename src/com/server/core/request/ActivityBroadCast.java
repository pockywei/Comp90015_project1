package com.server.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;

public class ActivityBroadCast extends AbstractRequest {

    private String username;
    private String message;

    public ActivityBroadCast(Connection connection, String username,
            String message) {
        super(connection);
        this.username = username;
        this.message = message;
    }

    @Override
    public Message getRequestMessage() {
        return Message.getBroadcastMsg(Message.getActivity(message, username),
                Command.ACTIVITY_BROADCAST);
    }

}
