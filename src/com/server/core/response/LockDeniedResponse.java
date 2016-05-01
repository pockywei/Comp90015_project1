package com.server.core.response;

import java.util.List;

import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.server.core.LocalStorage;
import com.server.core.ServerManager;

public class LockDeniedResponse extends AbstractResponse {

    private UserInfo user;

    public LockDeniedResponse(UserInfo user) {
        this.user = user;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        // will remove the username from local storage only if the secret is
        // also the same
        LocalStorage.getInstance().removeUser(user);
        log.info("respnose message lock denied, remove the user "
                + user.getUsername());
        Connection register = ServerManager.getInstance()
                .getRegisterConnection(user);
        if (register != null) {
            register.sendMessage(responseMsg(Command.REGISTER_FAILED,
                    String.format(Protocal.REGISTER_FAIL, user.getUsername())));
            ServerManager.getInstance().removeRegisterConnection(register);
            log.info("respnose message register failed. " + user.getUsername());
            return false;
        }

        // broadcast lock denied message to other servers.
        final List<Connection> servers = ServerManager.getInstance()
                .getAuthentiedServers();
        synchronized (servers) {
            for (Connection c : servers) {
                if (!c.equals(connection)) {
                    c.sendMessage(responseMsg(Command.LOCK_DENIED, user));
                }
            }
        }
        return false;
    }

}
