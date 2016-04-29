package com.client.core;

import com.client.UserSettings;
import com.client.core.request.LoginRequest;
import com.protocal.Message;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.Response;
import com.protocal.json.ParserJson;

public class ClientResponse implements Response {

    @Override
    public boolean process(String json, ConnectionListener connection)
            throws Exception {
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
                info = msg.getInfo();
                // Close connection when received these message.
                // Show the info onto GUI
                // TODO

                return true;
            case LOGIN_SUCCESS:
                info = msg.getInfo();
                // Show the info onto GUI
                // TODO

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
