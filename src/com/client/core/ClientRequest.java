package com.client.core;

import com.client.UserSettings;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.json.ParserJson;
import com.utils.UtilHelper;

public class ClientRequest  {

    private Command com;
    
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
//                ClientManger.getInstance().request(
//                        new Socket(msg.getHostnmae(), msg.getPort()),
//                        Command.LOGIN);
                return true;
            default:
                break;
        }
        return false;
    }

    protected Message getSendMsg() {
        switch (com) {
            case REGISTER:
            case LOGIN:
                return Message.getUserMsg(UserSettings.getUsername(),
                        UserSettings.getSecret(), com);
            case LOGOUT:
                return new Message(com);
            case ACTIVITY_MESSAGE:
//                return Message.getActivityMsg(UserSettings.getUsername(),
//                        UserSettings.getSecret(),
//                        UtilHelper.isEmptyStr("") ? "" : "", com);
            default:
                break;
        }
        return null;
    }


}
