package com.protocal.connection;

import java.io.IOException;
import java.net.Socket;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.json.JsonBuilder;
import com.protocal.json.ParserJson;
import com.server.ServerSettings;
import com.server.beans.ServerInfo;
import com.server.beans.UserInfo;
import com.server.core.ServerImpl;

public class Response extends Connection {

    public Response(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public boolean runTask() throws Exception {
        listening();
        return false;
    }

    /**
     * The read stream will be blocked until the socket time out or close.
     * 
     */
    protected void listening() {
        String message = null;
        boolean close = false;
        try {
            while (!close && ((message = read()) != null)) {
                close = process(message);
            }
            log.debug("connection closed to " + getSocketAddr());
        }
        catch (Exception e) {
            log.error("connection " + getSocketAddr()
                    + " closed with exception: " + e);
        }
        finally {
            close();
        }
    }

    @Override
    protected boolean process(String json) throws Exception {
        // process all case and write back a respnose.
        Message msg = new ParserJson(json).getMsg();
        String respnose = "";
        // if request data is not a json object, then reply as an invalid
        // message. Close connection.
        if (msg == null) {
            respnose = responseMsg(Command.INVALID_MESSAGE,
                    Command.INVALID_MESSAGE.getResponse());
            write(respnose);
            log.error("respnose message parsing exception. " + respnose);
            return true;
        }

        switch (msg.getCommand()) {
            case LOCK_REQUEST:
                // TODO

            case AUTHENTICATE:
                String secret = msg.getSecret();
                // secret doesn't match, response error and close connection.
                if (!ServerSettings.getLocalSecret().equals(secret)) {
                    respnose = responseMsg(Command.AUTHENTICATION_FAIL,
                            Command.AUTHENTICATION_FAIL.getResponse());
                    write(respnose);
                    log.error(
                            "respnose message parsing exception. " + respnose);
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

            case REGISTER:

            case LOGIN:

            case LOGOUT:

            case ACTIVITY_MESSAGE:

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
                        .getJson(com);
            case REDIRECT:
                return new JsonBuilder(Message.getRedirectMsg(
                        server.getHostname(), server.getPort(), com))
                                .getJson(com);
            case LOCK_DENIED:
                return new JsonBuilder(Message.getUserMsg(user.getUsername(),
                        user.getSecret(), com)).getJson(com);

            case LOCK_ALLOWED:
                return new JsonBuilder(
                        Message.getLockAllowedMsg(user.getUsername(),
                                user.getSecret(), server.getSecret(), com))
                                        .getJson(com);
            default:
                break;
        }
        return null;
    }

    @Override
    public void close() {
        super.close();
        ServerImpl.getInstance().removeConnection(this);
    }

}
