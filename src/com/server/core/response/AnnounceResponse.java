package com.server.core.response;

import com.beans.ServerInfo;
import com.protocal.connection.Connection;
import com.server.core.ServerManager;

public class AnnounceResponse extends AbstractResponse {

    private ServerInfo server;

    public AnnounceResponse(ServerInfo server) {
        this.server = server;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        // TODO Here is a bug for whether the announce has been authenticated.
        // if (!ServerManager.getInstance().hasServer(server)) {
        // connection.sendMessage(responseMsg(Command.INVALID_MESSAGE,
        // Protocal.HAS_NOE_AUTH));
        // log.error("respnose message server has not been authenticated.");
        // return true;
        // }
        // update local storge with the server info
        ServerManager.getInstance().updateServerInfo(server, connection);
        // broadcast to other adjacent servers
        ServerManager.getInstance().sendAnnounce(connection);
        return false;
    }

}
