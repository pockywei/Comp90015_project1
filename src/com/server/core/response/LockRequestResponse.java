package com.server.core.response;

import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.Connection;
import com.server.ServerSettings;
import com.server.core.database.LocalStorage;

public class LockRequestResponse extends RegisterResponse {

    private UserInfo register;

    public LockRequestResponse(UserInfo register) {
        super(register);
    }

    @Override
    public boolean process(Message msg, Connection root) throws Exception {
        // broadcast the lock request message to all other adjacent servers
        return lockBroadCast(root, register);
    }

    @Override
    protected boolean unlockMessage(Connection root, UserInfo register) {
        if (!LocalStorage.getInstance().hasUser(register)) {
            // send lock allowed message to root.
            LocalStorage.getInstance().addUser(register);
            root.sendMessage(responseMsg(Command.LOCK_ALLOWED, register,
                    ServerSettings.getLocalInfo()));
            log.info("respnose message lock allow to root. "
                    + register.getUsername());
        }
        else {
            // broadcast lock denied message to root.
            root.sendMessage(responseMsg(Command.LOCK_DENIED, register));
            log.info("respnose message lock denied to root. "
                    + register.getUsername());
        }
        return false;
    }
}
