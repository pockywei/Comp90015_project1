package com.client.core;

import java.io.IOException;
import java.net.Socket;

import com.client.UserSettings;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.Request;
import com.protocal.json.ParserJson;
import com.server.core.ServerRequest;
import com.utils.UtilHelper;

public class ClientRequest extends Request {

    public ClientRequest(Socket socket, Command com) throws IOException {
        super(socket, com);
    }

    public ClientRequest(Socket socket, Command com, String activity)
            throws IOException {
        super(socket, com, activity);
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
        return false;
    }

    @Override
    protected Message getSendMsg() {
        switch (com) {
            case REGISTER:
            case LOGIN:
                return Message.getUserMsg(UserSettings.getUsername(),
                        UserSettings.getSecret(), com);
            case LOGOUT:
                return new Message(com);
            case ACTIVITY_MESSAGE:
                return Message.getActivityMsg(UserSettings.getUsername(),
                        UserSettings.getSecret(),
                        UtilHelper.isEmptyStr(activity) ? "" : activity, com);
            default:
                break;
        }
        return null;
    }

    @Override
    public boolean nextMessage(Command com, String activity) {
        if (isClosed()) {
            return false;
        }

        try {
            post(new ClientRequest(getSocket(), com, activity));
        }
        catch (IOException e) {
            log.error("Send next message failed as the exception: " + e);
            return false;
        }
        return true;
    }
}
