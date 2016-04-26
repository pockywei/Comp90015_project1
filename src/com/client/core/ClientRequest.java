package com.client.core;

import java.io.IOException;
import java.net.Socket;

import com.client.UserSettings;
import com.utils.protocal.Command;
import com.utils.protocal.Message;
import com.utils.protocal.connection.Connection;
import com.utils.protocal.json.JsonBuilder;
import com.utils.protocal.json.ParserJson;

public class ClientRequest extends Connection {
    private String activity;

    public ClientRequest(Socket socket, Command com) throws IOException {
        super(socket, com);
    }

    public ClientRequest(Socket socket, Command com, String activity)
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
            case LOGIN_FAILED:
            case REGISTER_FAILED:
                String info = msg.getInfo();
                // Close connection when received these message.
                // Show the info onto GUI
                // TODO
                return true;
            case REGISTER_SUCCESS:
                // login...
                // TODO
                return true;
            case LOGIN_SUCCESS:
                info = msg.getInfo();
                // Show the info onto GUI
                // TODO
                return false;
            case REDIRECT:
                // Re-login to the given server.
                ClientManger.getInstance().request(
                        new Socket(msg.getHostnmae(), msg.getPort()),
                        Command.LOGIN);
                return true;
            default:
                break;
        }
        return true;
    }

    private Message getSendMsg() {
        switch (com) {
            case REGISTER:
            case LOGIN:
                return Message.getUserMsg(UserSettings.getUsername(),
                        UserSettings.getSecret(), com);
            case LOGOUT:
                return new Message(com);
            case ACTIVITY_MESSAGE:
                return Message.getActivityMsg(UserSettings.getUsername(),
                        UserSettings.getSecret(), activity, com);
            default:
                break;
        }
        return null;
    }
}
