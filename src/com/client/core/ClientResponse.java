package com.client.core;

import com.client.UserSettings;
import com.protocal.Message;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.Response;
import com.protocal.json.ParserJson;
import com.utils.log.Log;

public class ClientResponse implements Response {

    private static final Log log = Log.getInstance();

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        Message msg = new ParserJson(json).getMsg();
        if (msg == null) {
            log.error("received a empty response from the server...");
            return false;
        }
        String info = msg.getInfo();
        log.debug("received from the server " + info + " " + msg.getCommand());
        switch (msg.getCommand()) {
            case AUTHENTICATION_FAIL:
            case INVALID_MESSAGE:
            case LOGIN_FAILED:
            case REGISTER_FAILED:
                ClientManger.getInstance().notifyFrameFailed(msg.getCommand(),
                        info);
                return true;
            case REGISTER_SUCCESS:
                ClientManger.getInstance().notifyFrameSuccess(msg.getCommand(),
                        info);
                return true;
            case LOGIN_SUCCESS:
                ClientManger.getInstance().notifyFrameSuccess(msg.getCommand(),
                        info);
                return false;
            case REDIRECT:
                // Re-login to the given server.
                UserSettings.setServerInfo(msg.getPort(), msg.getHostnmae());
                ClientManger.getInstance().sendLoginRequest();
                return true;
            case ACTIVITY_BROADCAST:
                ClientManger.getInstance().notifyFrameSuccess(msg.getCommand(),
                        msg.getActivity().toString());
                return false;
            default:
                break;
        }
        return false;
    }

}
