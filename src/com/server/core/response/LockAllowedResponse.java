package com.server.core.response;

import java.util.List;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.server.core.LocalStorage;
import com.server.core.ServerManager;

public class LockAllowedResponse extends AbstractResponse {

    private UserInfo user;
    private ServerInfo server;

    public LockAllowedResponse(UserInfo user, ServerInfo server) {
        this.user = user;
        this.server = server;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        // if all servers are lock allowed, then send back register success.
        final List<Connection> servers = ServerManager.getInstance()
                .getAuthentiedServers();
        synchronized (servers) {
            for (Connection c : servers) {
                ServerInfo serverInfo = (ServerInfo) c.getConnectionInfo();
                if (server.getId().equals(serverInfo.getId())) {
                    Connection register = ServerManager.getInstance()
                            .getRegisterConnection(user);
                    if (register != null) {
                        log.info("respnose message lock allowed wait number: "
                                + register.waitCount);
                        if (--register.waitCount == 0) {
                            LocalStorage.getInstance().addUser(user);
                            register.sendMessage(
                                    responseMsg(Command.REGISTER_SUCCESS,
                                            String.format(
                                                    Protocal.REGISTER_SUCC,
                                                    user.getUsername())));
                            ServerManager.getInstance()
                                    .removeRegisterConnection(register);
                            log.info("respnose message register success. "
                                    + user.getUsername());
                        }
                    }
                    break;
                }
            }
        }
        return false;
    }

}
