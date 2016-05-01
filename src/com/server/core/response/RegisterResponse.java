package com.server.core.response;

import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.server.core.LocalStorage;
import com.server.core.ServerManager;

public class RegisterResponse extends AbstractResponse {

    private UserInfo register;

    public RegisterResponse(UserInfo register) {
        this.register = register;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        // check local storage
        if (!LocalStorage.getInstance().hasUser(register)) {
            // local storage does not have the register user, sending
            // lock request to other servers.
            connection.waitCount = ServerManager.getInstance().sendLockRequest(
                    connection, register.getUsername(), register.getSecret());

            // keep the register connection until all servers reply lock
            // allowed.
            ServerManager.getInstance().addRegisterConnection(connection);
            return false;
        }
        // the user has registered on the system.
        connection.sendMessage(responseMsg(Command.REGISTER_FAILED,
                String.format(Protocal.REGISTER_FAIL, register.getUsername())));
        log.error("respnose message the user has registered on the system. "
                + register.getUsername());
        return true;
    }

}