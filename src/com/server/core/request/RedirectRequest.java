package com.server.core.request;

import com.beans.ServerInfo;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.AbstractRequest;
import com.protocal.connection.Connection;

public class RedirectRequest extends AbstractRequest {

    private ServerInfo server;
    private Command com;

    public RedirectRequest(Connection connection, ServerInfo server,
            Command com) {
        super(connection);
        this.server = server;
        this.com = com;
    }

    @Override
    public Message getRequestMessage() {
        switch (com) {
            case REDIRECT:
                return Message.getRedirectMsg(server.getHostname(),
                        server.getPort(), com);
            case REAUTHENTICATE:
                return Message.getReAuthenticateMsg(server.getHostname(),
                        server.getPort(), com, server.getSecret());
            default:
                break;
        }
        return null;
    }
}
