package com.server.core.response;

import com.beans.UserInfo;
import com.protocal.connection.Connection;
import com.server.core.ServerManager;

public class BroadcastResponse extends AbstractResponse {

    private UserInfo user;
    private String message;

    public BroadcastResponse(UserInfo user, String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        ServerManager.getInstance().sendActivityBroadcast(connection,
                user.getUsername(), message);
        return false;
    }

}
