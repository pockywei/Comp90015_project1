package com.client.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;

public class ActivityMessage extends AbstractRequest{

    @Override
    public Message getRequestMessage(Command comm) {
        return null;
    }

}