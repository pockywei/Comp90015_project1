package com.server.core;

import java.util.List;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.Response;
import com.protocal.json.JsonBuilder;
import com.protocal.json.ParserJson;
import com.server.ServerSettings;
import com.utils.log.Log;

public class ServerResponse implements Response {

    private static final Log log = Log.getInstance();

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        Message msg = new ParserJson(json).getMsg();
        if (msg == null) {
            connection.sendMessage(responseMsg(Command.INVALID_MESSAGE,
                    Protocal.INVALIED_ERROR));
            log.error("respnose message parsing exception. close connection.");
            return true;
        }

        if (msg.getCommand() == null) {
            connection.sendMessage(responseMsg(Command.INVALID_MESSAGE,
                    String.format(Protocal.ERROR_PARSE, Protocal.COMMAND)));
            log.error("respnose message no command. close connection.");
            return true;
        }

        switch (msg.getCommand()) {
            /*
             * response to the server side.
             */
            case LOCK_REQUEST:
                // TODO
                return false;
            case AUTHENTICATE:
                String secret = msg.getSecret();
                // secret doesn't match, response error and close connection.
                if (!ServerSettings.getLocalSecret().equals(secret)) {
                    // respnose = responseMsg(Command.AUTHENTICATION_FAIL,
                    // Command.AUTHENTICATION_FAIL.getResponse());
                    // write(respnose);
                    // log.error(
                    // "respnose message parsing exception. " + respnose);
                    return true;
                }
                // if succeed, reply nothing and keep the connection alive.
                return false;
            case SERVER_ANNOUNCE:
                // the secret of the server.
                String id = msg.getId();

                // if succeed, reply nothing and keep the connection alive.

                return false;
            case ACTIVITY_BROADCAST:

                return false;
            /*
             * response to the client side.
             */
            case REGISTER:
                UserInfo register = msg.toUserInfo();
                // check local storage
                if (LocalStorage.getInstance().addUser(register)) {
                    // local storage does not have the register user, sending
                    // lock request to other servers.

                    // wait for a while to collect the lock allowed message or
                    // lock denied message.
                    Thread.sleep(Protocal.WAIT_FOR_LOCK_RESPONSE);

                    // gather lock allowed messages

                    // if all lock allowed, register success.
                    if (true) {
                        connection.sendMessage(
                                responseMsg(Command.REGISTER_SUCCESS,
                                        String.format(Protocal.REGISTER_SUCC,
                                                register.getUsername())));
                        log.error("respnose message register success. "
                                + register.getUsername());
                        return true;
                    }
                }
                // the user has registered on the system.
                connection.sendMessage(responseMsg(Command.REGISTER_FAILED,
                        String.format(Protocal.REGISTER_FAIL,
                                register.getUsername())));
                log.error(
                        "respnose message the user has registered on the system. "
                                + register.getUsername());
                return true;
            case LOGIN:
                UserInfo loginUser = msg.toUserInfo();
                if (!LocalStorage.getInstance().addUser(loginUser)) {
                    
                }

                return false;
            case LOGOUT:

                return true;
            case ACTIVITY_MESSAGE:

                return false;
            default:
                break;
        }
        return false;
    }

    private String responseMsg(Command com, String info) {
        return responseMsg(com, info, null, null);
    }

    private String responseMsg(Command com, ServerInfo server) {
        return responseMsg(com, null, null, server);
    }

    private String responseMsg(Command com, UserInfo user) {
        return responseMsg(com, null, user, null);
    }

    private String responseMsg(Command com, UserInfo user, ServerInfo server) {
        return responseMsg(com, null, user, server);
    }

    private String responseMsg(Command com, String info, UserInfo user,
            ServerInfo server) {
        switch (com) {
            case REGISTER_FAILED:
            case REGISTER_SUCCESS:
            case LOGIN_SUCCESS:
            case LOGIN_FAILED:
            case AUTHENTICATION_FAIL:
            case INVALID_MESSAGE:
                return new JsonBuilder(Message.getResponseMsg(info, com))
                        .getJson();
            case REDIRECT:
                return new JsonBuilder(Message.getRedirectMsg(
                        server.getHostname(), server.getPort(), com)).getJson();
            case LOCK_DENIED:
                return new JsonBuilder(Message.getUserMsg(user.getUsername(),
                        user.getSecret(), com)).getJson();

            case LOCK_ALLOWED:
                return new JsonBuilder(
                        Message.getLockAllowedMsg(user.getUsername(),
                                user.getSecret(), server.getId(), com))
                                        .getJson();
            default:
                break;
        }
        return null;
    }

}
