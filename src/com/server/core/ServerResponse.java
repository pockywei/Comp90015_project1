package com.server.core;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.protocal.json.ParserJson;
import com.server.core.response.AbstractResponse;
import com.server.core.response.AnnounceResponse;
import com.server.core.response.AuthenticateResponse;
import com.server.core.response.AvtivityMessageResponse;
import com.server.core.response.BroadcastResponse;
import com.server.core.response.LockAllowedResponse;
import com.server.core.response.LockDeniedResponse;
import com.server.core.response.LockResponse;
import com.server.core.response.LoginResponse;
import com.server.core.response.RegisterResponse;
import com.utils.log.CrashHandler;

public class ServerResponse extends AbstractResponse {

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
                return new LockResponse(msg.toUserInfo()).process(json,
                        connection);
            case AUTHENTICATE:
                return new AuthenticateResponse(msg.toServerInfo())
                        .process(json, connection);
            case SERVER_ANNOUNCE:
                return new AnnounceResponse(msg.toServerInfo()).process(json,
                        connection);
            case ACTIVITY_BROADCAST:
                return new BroadcastResponse(msg.getActivity().getUsername(),
                        msg.getActivity().getMessage()).process(json,
                                connection);
            case LOCK_ALLOWED:
                return new LockAllowedResponse(msg.toUserInfo(),
                        msg.toServerInfo()).process(json, connection);
            case LOCK_DENIED:
                return new LockDeniedResponse(msg.toUserInfo()).process(json,
                        connection);
            case AUTHENTICATION_FAIL:
            case INVALID_MESSAGE:
                log.error(msg.getInfo());
                if (connection != null
                        && connection.getType() == ConnectionType.SERVER_CONN) {
                    // authenticate failed
                    CrashHandler.getInstance().errorExit();
                }
                return true;
            /*
             * response to the client side.
             */
            case REGISTER:
                return new RegisterResponse(msg.toUserInfo()).process(json,
                        connection);
            case LOGIN:
                return new LoginResponse(msg.toUserInfo()).process(json,
                        connection);
            case LOGOUT:
                // close connection.
                return true;
            case ACTIVITY_MESSAGE:
                return new AvtivityMessageResponse(msg.toUserInfo(),
                        msg.getActivity().getMessage()).process(json,
                                connection);
            default:
                break;
        }
        // no cases matched, close connection.
        return true;
    }

}
