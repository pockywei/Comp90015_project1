package com.server.core.response;

import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.server.core.LocalStorage;
import com.server.core.ServerManager;

public class AvtivityMessageResponse extends AbstractResponse {

    private UserInfo user;
    private String message;

    public AvtivityMessageResponse(UserInfo user, String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        // check authenticate.
        if (LocalStorage.getInstance().hasUser(user)
                && LocalStorage.getInstance().loginCheck(user)
                && ServerManager.getInstance().isUserLogged(user)) {
            // send a message broadcast to all adjacent servers and
            // clients.
            ServerManager.getInstance().sendActivityBroadcast(connection,
                    user.getUsername(), message);
            return false;
        }
        connection.sendMessage(responseMsg(Command.AUTHENTICATION_FAIL,
                Protocal.AUTH_LOGIN_FAIL));
        log.error("respnose message authenticate fail, user "
                + user.getUsername());
        return true;
    }

}
