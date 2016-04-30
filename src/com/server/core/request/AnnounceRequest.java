package com.server.core.request;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;
import com.server.ServerSettings;

public class AnnounceRequest extends AbstractRequest {

    public AnnounceRequest(Connection connection) {
        super(connection);
    }

    @Override
    public Message getRequestMessage() {
        return Message.getAnnounceMsg(ServerSettings.getLocalID(),
                ServerSettings.getLocalLoad(),
                ServerSettings.getLocalHostname(),
                ServerSettings.getLocalPort(), Command.SERVER_ANNOUNCE);
    }
}
