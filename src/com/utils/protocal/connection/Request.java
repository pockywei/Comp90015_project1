package com.utils.protocal.connection;

import java.io.IOException;
import java.net.Socket;

import com.client.UserSettings;
import com.server.ServerSettings;
import com.utils.protocal.Command;
import com.utils.protocal.Message;
import com.utils.protocal.json.JsonBuilder;
import com.utils.protocal.json.ParserJson;

public class Request extends Connection {

    private String activity;

    public Request(Socket socket, Command com) throws IOException {
        super(socket, com);
    }

    public Request(Socket socket, Command com, String activity)
            throws IOException {
        super(socket, com);
        this.activity = activity;
    }

    @Override
    public void runTask() throws Exception {
        Message msg = getSendMsg();
        if (msg == null) {
            log.error("sending message error by command " + com);
            throw new Exception();
        }
        String json = new JsonBuilder(msg).getJson(com);
        log.debug("request json: " + json + " by " + com);
        write(json);
        read();
    }

    @Override
    protected boolean process(String json) throws Exception {
        Message msg = new ParserJson(json).getMsg();
        switch (msg.getCommand()) {
            case INVALID_MESSAGE:
            case AUTHENTICATION_FAIL:
                String info = msg.getInfo();
                // Close connection when received these message.
                // Show the info onto GUI
                // TODO
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
        return true;
    }

    private Message getSendMsg() {
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
                return Message.getBroadcastMsg(activity, com);
            default:
                break;
        }
        return null;
    }
}
