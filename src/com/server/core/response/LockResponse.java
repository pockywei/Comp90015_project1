package com.server.core.response;

import java.util.List;

import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.server.ServerSettings;
import com.server.core.ServerManager;
import com.server.core.database.LocalStorage;
import com.server.core.listener.LockState;

public class LockResponse extends AbstractResponse {

    private UserInfo user;
    private boolean isAllow;

    public LockResponse(UserInfo user, boolean isAllow) {
        this.user = user;
        this.isAllow = isAllow;
    }

    @Override
    public boolean process(Message msg, Connection connection)
            throws Exception {
        // if all servers are lock allowed, then send back register success.
        int waitCount = connection.reduceCount(isAllow, user);
        log.info("got a lockallowed message for user: " + user.getUsername()
                + " wait count: " + waitCount);
        LockState state = connection.hasFinishLock(user);
        // still waiting until the outcome is finished
        if (state == LockState.WAITTED) {
            return false;
        }
        Connection root = ServerManager.getInstance().getRootConnection(user);
        if (root != null) {
            // sub server has finished the waiting from the leaf nodes, then
            // send back to root.
            log.info("lock waitting is finished : " + state + " for user: "
                    + user.getUsername());
            if (state == LockState.ALLOWED) {
                sendAllowToRoot(root, user);
            }
            else if (state == LockState.DENIED) {
                sendDeniedBroadcast(root, user);
            }
            ServerManager.getInstance().removeRootConnection(user);
            return false;
        }
        // top server which is connected by the clients
        Connection client = ServerManager.getInstance()
                .getRegisterConnection(user);
        if (client != null) {
            log.info("top root server lock waitting is finished : " + state
                    + " for user: " + user.getUsername());
            String response = null;
            if (state == LockState.ALLOWED) {
                response = String.format(Protocal.REGISTER_SUCC,
                        user.getUsername());
                ServerManager.getInstance().registerSuccess(client, user,
                        responseMsg(Command.REGISTER_SUCCESS, response));
            }
            else if (state == LockState.DENIED) {
                response = String.format(Protocal.REGISTER_FAIL,
                        user.getUsername());
                ServerManager.getInstance().registerFailed(client,
                        responseMsg(Command.REGISTER_SUCCESS, response));
            }
            log.info(response);
        }
        return true;
    }

    protected int sendDeniedBroadcast(Connection from, UserInfo user) {
        LocalStorage.getInstance().removeUser(user);
        log.info("respnose message lock denied, remove the user "
                + user.getUsername());
        List<Connection> servers = ServerManager.getInstance()
                .getAuthenticatedServers();
        int count = 0;
        synchronized (servers) {
            for (Connection c : servers) {
                if (!c.equals(from)) {
                    c.sendMessage(responseMsg(Command.LOCK_DENIED, user));
                }
            }
        }
        return count;
    }

    protected void sendAllowToRoot(Connection root, UserInfo register) {
        LocalStorage.getInstance().addUser(register);
        root.sendMessage(responseMsg(Command.LOCK_ALLOWED, register,
                ServerSettings.getLocalInfo()));
        log.info("respnose message lock allow to root. "
                + register.getUsername());
    }
}
