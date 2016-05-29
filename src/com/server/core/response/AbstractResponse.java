package com.server.core.response;

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

public abstract class AbstractResponse implements Response {

    protected static final Log log = Log.getInstance();

    @Override
    public boolean preProcess(String json, Connection connection)
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
        return process(msg, connection);
    }

    protected String responseMsg(Command com, String info, UserInfo user,
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
            case SECRET_REQUEST:
                return new JsonBuilder(Message.getLocalSecret(com,
                        ServerSettings.getLocalHostname(),
                        ServerSettings.getLocalPort(),
                        ServerSettings.getLocalSecret())).getJson();
            default:
                break;
        }
        return null;
    }

    public abstract boolean process(Message msg, Connection connection)
            throws Exception;

    protected String responseMsg(Command com, String info) {
        return responseMsg(com, info, null, null);
    }

    protected String responseMsg(Command com, ServerInfo server) {
        return responseMsg(com, null, null, server);
    }

    protected String responseMsg(Command com, UserInfo user) {
        return responseMsg(com, null, user, null);
    }

    protected String responseMsg(Command com, UserInfo user,
            ServerInfo server) {
        return responseMsg(com, null, user, server);
    }

    protected String responseMsg(Command com) {
        return responseMsg(com, null, null, null);
    }
}
