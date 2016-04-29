package com.client.core;

import com.client.UserSettings;
import com.client.core.inter.FrameUpdateListener;
import com.client.core.request.LoginRequest;
import com.protocal.Message;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.Response;
import com.protocal.json.ParserJson;
import com.utils.log.Log;

public class ClientResponse implements Response {

    private static final Log log = Log.getInstance();

    @Override
    public boolean process(String json, ConnectionListener connection)
            throws Exception {
        Message msg = new ParserJson(json).getMsg();
        switch (msg.getCommand()) {
            case INVALID_MESSAGE:
            case LOGIN_FAILED:
            case REGISTER_FAILED:
                String info = msg.getInfo();
                log.debug(info + " " + msg.getCommand());
                ClientManger.getInstance().notifyFrameFailed(info);
                return true;
            case REGISTER_SUCCESS:
                info = msg.getInfo();
                log.debug(info + " " + msg.getCommand());
                ClientManger.getInstance().notifyFrameSuccess(msg.getCommand(),
                        info);
                return true;
            case LOGIN_SUCCESS:
                info = msg.getInfo();
                log.debug(info + " " + msg.getCommand());
                ClientManger.getInstance().notifyFrameSuccess(msg.getCommand(),
                        info);
                return false;
            case REDIRECT:
                // Re-login to the given server.
                UserSettings.resetServerInfo(msg.getPort(), msg.getHostnmae());
                new LoginRequest(ClientManger.getInstance().createConnection(),
                        UserSettings.getUsername(), UserSettings.getSecret())
                                .request();
                return true;
            default:
                break;
        }
        return false;
    }

}
