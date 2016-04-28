package com.server.core;

import java.io.IOException;
import java.net.Socket;

import com.client.UserSettings;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.Request;
import com.protocal.json.ParserJson;
import com.server.ServerSettings;
import com.utils.UtilHelper;

public class ServerRequest extends Request {

    public ServerRequest(Socket socket, Command com) throws IOException {
        super(socket, com);
    }

    public ServerRequest(Socket socket, Command com, String activity)
            throws IOException {
        super(socket, com, activity);
    }

    @Override
    protected boolean process(String json) throws Exception {
        Message msg = new ParserJson(json).getMsg();
        log.debug("response json: " + json + " by " + msg.getCommand());
        switch (msg.getCommand()) {
            case INVALID_MESSAGE:
            case AUTHENTICATION_FAIL:
                String info = msg.getInfo(); 
                log.info(info);
                return true;
            case LOCK_DENIED:
                // Remove the username from local storage

                return false;
            case LOCK_ALLOWED:
                // If all servers allow, then close connection
                // sending register success to client
                // TODO
                // if () {
                // return true;
                // }
                return false;
            default:
                break;
        }
        return false;
    }

    @Override
    protected Message getSendMsg() {
        switch (com) {
            case LOCK_REQUEST:
                return Message.getUserMsg(UserSettings.getUsername(),
                        UserSettings.getSecret(), com);
            case AUTHENTICATE:
                return Message.getAuthenticateMsg(
                        ServerSettings.getRemoteSecret(), com);
            case SERVER_ANNOUNCE:
                return Message.getAnnounceMsg(ServerSettings.getLocalSecret(),
                        ServerSettings.getLocalLoad(),
                        ServerSettings.getLocalHostname(),
                        ServerSettings.getLocalPort(), com);
            case ACTIVITY_BROADCAST:
                return Message.getBroadcastMsg(
                        UtilHelper.isEmptyStr(activity) ? "" : activity, com);
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

    @Override
    public boolean nextMessage(Command com, String activity) {
        if (isClosed()) {
            return false;
        }

        try {
            post(new ServerRequest(getSocket(), com, activity));
        }
        catch (IOException e) {
            log.error("Send next message failed as the exception: " + e);
            return false;
        }
        return true;
    }
}
