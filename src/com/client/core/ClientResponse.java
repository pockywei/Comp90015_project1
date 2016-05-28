package com.client.core;

import com.client.UserSettings;
import com.client.core.inter.FrameUpdateListener;
import com.protocal.Message;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.Response;
import com.protocal.json.ParserJson;
import com.utils.log.Log;

public class ClientResponse implements Response {

    private static final Log log = Log.getInstance();
    private FrameUpdateListener listener = null;

    public ClientResponse(FrameUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean preProcess(String json, Connection connection)
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
                if (listener != null) {
                    listener.actionFailed(msg.getCommand(), info);
                }
                return true;
            case REGISTER_SUCCESS:
                if (listener != null) {
                    listener.actionSuccess(msg.getCommand(), info, null);
                }
                return true;
            case LOGIN_SUCCESS:
                if (listener != null) {
                    listener.actionSuccess(msg.getCommand(), info, null);
                }
                return false;
            case REDIRECT:
                // Re-login to the given server.
                UserSettings.setServerInfo(msg.getPort(), msg.getHostnmae());
                ClientManger.getInstance().sendLoginRequest(null);
                return true;
            case ACTIVITY_BROADCAST:
                if (listener != null) {
                    listener.actionSuccess(msg.getCommand(),
                            msg.getActivity().getUsername(), msg.getActivity());
                }
                return false;
            default:
                break;
        }
        return false;
    }

}
