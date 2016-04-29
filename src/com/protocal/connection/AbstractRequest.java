package com.protocal.connection;

import com.protocal.Command;
import com.protocal.Message;

public abstract class AbstractRequest {

    public abstract Message getRequestMessage(Command comm);
}
