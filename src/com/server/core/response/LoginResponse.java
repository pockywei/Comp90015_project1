package com.server.core.response;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.server.core.LocalStorage;
import com.server.core.ServerManager;

public class LoginResponse extends AbstractResponse {

    private UserInfo loginUser;

    public LoginResponse(UserInfo loginUser) {
        this.loginUser = loginUser;
    }

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        if (LocalStorage.getInstance().hasUser(loginUser)) {
            if (LocalStorage.getInstance().loginCheck(loginUser)) {
                
                connection.sendMessage(responseMsg(Command.LOGIN_SUCCESS, String
                        .format(Protocal.LOGIN_SUCC, loginUser.getUsername())));
                log.debug("respnose message user login success. "
                        + loginUser.getUsername());

                // check whether it need to be redirected to another
                // server.
                ServerInfo redirectServer = ServerManager.getInstance()
                        .redirect();
                if (redirectServer != null) {
                    connection.sendMessage(
                            responseMsg(Command.REDIRECT, redirectServer));
                    log.debug("respnose message user redirect. "
                            + loginUser.getUsername() + " "
                            + redirectServer.getKey());
                    // close connection.
                    return true;
                }

                // add the connection to the ServerManager
                connection.classifyConnectionType(ConnectionType.USER_CONN,
                        loginUser);
                return false;
            }
            else {
                connection.sendMessage(responseMsg(Command.LOGIN_FAILED, String
                        .format(Protocal.LOGIN_FAIL, loginUser.getUsername())));
                log.error("respnose message user secret does not match "
                        + loginUser.getUsername());
                return true;
            }
        }
        // does not have this user. login failed.
        connection.sendMessage(responseMsg(Command.LOGIN_FAILED, String.format(
                Protocal.REGISTER_FAIL_NO_USER, loginUser.getUsername())));
        log.error("respnose message no user " + loginUser.getUsername());
        return true;
    }

}
