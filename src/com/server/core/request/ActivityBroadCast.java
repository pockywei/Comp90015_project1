package com.server.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;

public class ActivityBroadCast extends AbstractRequest{

    @Override
    public Message getRequestMessage(Command comm) {
        return null;
    }

}