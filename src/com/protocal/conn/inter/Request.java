package com.protocal.conn.inter;

import com.protocal.Command;
import com.protocal.Message;

public interface Request {

    public Message getRequestMessage(Command comm);
}
