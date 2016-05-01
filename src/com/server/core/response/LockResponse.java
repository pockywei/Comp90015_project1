package com.server.core.response;

import java.util.List;

import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.connection.Connection;
import com.server.ServerSettings;
import com.server.core.LocalStorage;
import com.server.core.ServerManager;

public class LockResponse extends AbstractResponse {

    private UserInfo user;

    public LockResponse(UserInfo user) {
        this.user = user;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {

        if (!LocalStorage.getInstance().hasUser(user)) {
            // send lock allowed message to other servers.
            LocalStorage.getInstance().addUser(user);
            // broadcast this lock allowed message
            final List<Connection> servers = ServerManager.getInstance()
                    .getAuthentiedServers();
            synchronized (servers) {
                for (Connection c : servers) {
                    if (!c.equals(connection)) {
                        connection.sendMessage(responseMsg(Command.LOCK_ALLOWED,
                                user, ServerSettings.getLocalInfo()));
                    }
                }
            }
            log.info("respnose message lock allow. " + user.getUsername());
        }
        else {
            // broadcast lock denied message to other servers.
            final List<Connection> servers = ServerManager.getInstance()
                    .getAuthentiedServers();
            synchronized (servers) {
                for (Connection c : servers) {
                    if (!c.equals(connection)) {
                        connection.sendMessage(
                                responseMsg(Command.LOCK_DENIED, user));
                    }
                }
            }
            log.info("respnose message lock denied. " + user.getUsername());
        }
        return false;
    }

}
