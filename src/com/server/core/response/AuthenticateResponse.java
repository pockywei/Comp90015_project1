package com.server.core.response;

import com.beans.ServerInfo;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.server.ServerSettings;
import com.server.core.ServerManager;

public class AuthenticateResponse extends AbstractResponse {

    private ServerInfo server;

    public AuthenticateResponse(ServerInfo server) {
        this.server = server;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        if (ServerManager.getInstance().hasServer(server)) {
            connection.sendMessage(
                    responseMsg(Command.INVALID_MESSAGE, Protocal.HAS_AUTH));
            log.error(
                    "respnose message server authenticate failed, has been authenticated.");
            return true;
        }
        // if the supplied secret is the same with the local secret, then
        // success.
        if (server.getSecret().equals(ServerSettings.getLocalSecret())) {
            connection.classifyConnectionType(ConnectionType.SERVER_CONN,
                    server);
            log.debug("respnose message server authenicate success.");
            return false;
        }
        // server authenticate failed.
        connection.sendMessage(responseMsg(Command.AUTHENTICATION_FAIL,
                String.format(Protocal.AUTH_FAIL, server.getSecret())));
        log.error("respnose message server authenticate failed "
                + server.getSecret());
        return true;
    }

}
