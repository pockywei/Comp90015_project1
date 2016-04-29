package com.server.core;

import com.client.UserSettings;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.json.ParserJson;
import com.server.ServerSettings;
import com.utils.UtilHelper;
import com.utils.log.Log;

public class ServerRequest {
    private static final Log log = Log.getInstance();
    private Command com;
        
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
//                return Message.getBroadcastMsg(
//                        UtilHelper.isEmptyStr("") ? "" : "", com);
            default:
                break;
        }
        return null;
    }

}
