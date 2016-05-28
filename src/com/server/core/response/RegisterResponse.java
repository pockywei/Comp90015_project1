package com.server.core.response;

import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.server.core.ServerManager;
import com.server.core.database.LocalStorage;

public class RegisterResponse extends AbstractResponse {

    protected UserInfo register;

    public RegisterResponse(UserInfo register) {
        this.register = register;
    }

    @Override
    public boolean process(Message msg, Connection client) throws Exception {
        String response;
        // check local storage
        if (!LocalStorage.getInstance().hasUser(register)) {
            client.setConnectionInfo(register);
            return lockBroadCast(client, register);
        }
        // the user has registered on the system.
        response = String.format(Protocal.REGISTER_FAIL,
                register.getUsername());
        client.sendMessage(responseMsg(Command.REGISTER_FAILED, response));
        log.error(response);
        return true;
    }

    protected boolean lockBroadCast(Connection root, UserInfo register) {
        // broadcast a lock request message to other adjacent servers
        int waitCount = ServerManager.getInstance().sendLockRequest(root,
                register.getUsername(), register.getSecret());
        // if it is a leaf node or single root server which has no any other sub
        // servers. then send back success/allowed/denied
        if (waitCount == 0) {
            return unlockMessage(root, register);
        }
        root.setWaitState(waitCount, register);
        // record either root connection reference or all client connection
        // references
        if (root.getConnectionInfo() instanceof UserInfo) {
            // keep the register client connections until all servers reply lock
            // allowed.
            ServerManager.getInstance().addRegisterConnection(root);
            log.info(
                    "root server will be waiting for lock response from the adjacent servers");
        }
        else {
            // root connection wait for lock allow or denied
            ServerManager.getInstance().setRootConnection(root, register);
            log.info(
                    "sub servers record the root connection reference and waiting");
        }
        return false;
    }

    /**
     * if the node is a single server or a leaf node, then send message to
     * client or root node.
     * 
     * @param root
     * @param register
     * @return
     */
    protected boolean unlockMessage(Connection root, UserInfo register) {
        String response = String.format(Protocal.REGISTER_SUCC,
                register.getUsername());
        ServerManager.getInstance().registerSuccess(root, register,
                responseMsg(Command.REGISTER_SUCCESS, response));
        log.info(response);
        // if root register connection sends success message, then close the
        // connection.
        return true;
    }
}
