package com.server.core.response;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.server.core.ServerManager;
import com.server.core.database.LocalStorage;

public class LoginResponse extends AbstractResponse {

    private UserInfo loginUser;

    public LoginResponse(UserInfo loginUser) {
        this.loginUser = loginUser;
    }

    @Override
    public boolean process(Message msg, Connection connection)
            throws Exception {
        String response;
        if (LocalStorage.getInstance().hasUser(loginUser)) {
            if (LocalStorage.getInstance().loginCheck(loginUser)) {
                response = String.format(Protocal.LOGIN_SUCC,
                        loginUser.getUsername());
                connection.sendMessage(
                        responseMsg(Command.LOGIN_SUCCESS, response));
                log.debug(response);

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
                connection.classifyType(ConnectionType.USER_CONN,
                        loginUser);
                return false;
            }
            else {
                response = String.format(Protocal.LOGIN_FAIL,
                        loginUser.getUsername());
                connection.sendMessage(
                        responseMsg(Command.LOGIN_FAILED, response));
                log.error("respnose message user secret does not match "
                        + loginUser.getUsername());
                return true;
            }
        }
        // does not have this user. login failed.
        response = String.format(Protocal.REGISTER_FAIL_NO_USER,
                loginUser.getUsername());
        connection.sendMessage(responseMsg(Command.LOGIN_FAILED, response));
        log.error(response);
        return true;
    }

}
