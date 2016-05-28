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
        Connection root = ServerManager.getInstance().getRootConnection(user);
        if (root == null) {
            // top server which is connected by the clients
            root = ServerManager.getInstance().getRegisterConnection(user);
        }
        LockState state = getLockState(root, user);
        // still waiting until the outcome is finished
        if (state == LockState.WAITTED) {
            return false;
        }
        // sub server has finished the waiting from the leaf nodes, then
        // send back to root.
        log.info("lock waitting is finished : " + state + " for user: "
                + user.getUsername());
        if (state == LockState.ALLOWED) {
            sendAllow(root, user);
        }
        else if (state == LockState.DENIED) {
            sendDenied(root, user);
        }
        // register success for the client, close the connection
        if (root.getConnectionInfo() instanceof UserInfo) {
            return false;
        }
        // server will remove the root connection reference
        ServerManager.getInstance().removeRootConnection(user);
        return false;
    }

    private LockState getLockState(Connection root, UserInfo user) {
        int waitCount = root.reduceCount(isAllow, user);
        log.info("got a lockallowed message for user: " + user.getUsername()
                + " wait count: " + waitCount);
        return root.hasFinishLock(user);
    }

    protected void sendDenied(Connection from, UserInfo user) {
        // connect to client
        if (from.getConnectionInfo() instanceof UserInfo) {
            String response = String.format(Protocal.REGISTER_FAIL,
                    user.getUsername());
            ServerManager.getInstance().registerFailed(from,
                    responseMsg(Command.REGISTER_FAILED, response));
            log.info(response);
            return;
        }
        // connect to root server
        LocalStorage.getInstance().removeUser(user);
        log.info("respnose message lock denied, remove the user "
                + user.getUsername());
        List<Connection> servers = ServerManager.getInstance()
                .getAuthenticatedServers();
        synchronized (servers) {
            for (Connection c : servers) {
                if (!c.equals(from)) {
                    c.sendMessage(responseMsg(Command.LOCK_DENIED, user));
                }
            }
        }
    }

    protected void sendAllow(Connection root, UserInfo register) {
        if (root.getConnectionInfo() instanceof UserInfo) {
            String response = String.format(Protocal.REGISTER_SUCC,
                    user.getUsername());
            ServerManager.getInstance().registerSuccess(root, user,
                    responseMsg(Command.REGISTER_SUCCESS, response));
            log.info(response);
            return;
        }
        LocalStorage.getInstance().addUser(register);
        root.sendMessage(responseMsg(Command.LOCK_ALLOWED, register,
                ServerSettings.getLocalInfo()));
        log.info("respnose message lock allow to root. "
                + register.getUsername());
    }
}
