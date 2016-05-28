package com.server.core.response;

import com.beans.ServerInfo;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.server.ServerSettings;

public class AuthenticateResponse extends AbstractResponse {

    private ServerInfo server;

    public AuthenticateResponse(ServerInfo server) {
        this.server = server;
    }

    @Override
    public boolean process(Message msg, Connection connection)
            throws Exception {
        // if the supplied secret is the same with the local secret, then
        // success.
        if (server.getSecret().equals(ServerSettings.getLocalSecret())) {
            connection.classifyType(ConnectionType.SERVER_CONN,
                    server);
            log.debug("respnose message server authenicate success.");
            return false;
        }
        // server authenticate failed.
        String response = String.format(Protocal.AUTH_FAIL, server.getSecret());
        connection.sendMessage(
                responseMsg(Command.AUTHENTICATION_FAIL, response));
        log.error(response);
        return true;
    }
}
