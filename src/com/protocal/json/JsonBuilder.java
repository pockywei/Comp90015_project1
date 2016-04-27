package com.protocal.json;

import com.google.gson.JsonObject;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.Protocal;

public class JsonBuilder {

    private Message msg;

    public JsonBuilder(Message msg) {
        this.msg = msg;
    }
    
    public String getJson(Command com) {
        JsonObject json = new JsonObject();
        json.addProperty(Protocal.COMMAND, com.name());
        switch (com) {
            case LOGIN_FAILED:
            case LOGIN_SUCCESS:
            case AUTHENTICATION_FAIL:
            case REGISTER_FAILED:
            case INVALID_MESSAGE:
            case REGISTER_SUCCESS:
                json.addProperty(Protocal.INFO, msg.getInfo());
                break;
            case AUTHENTICATE:
                json.addProperty(Protocal.SECRET, msg.getSecret());
                break;
            case LOCK_REQUEST:
            case REGISTER:
            case LOGIN:
            case LOCK_DENIED:
                json.addProperty(Protocal.USER_NAME, msg.getUsername());
                json.addProperty(Protocal.SECRET, msg.getSecret());
                break;
            case LOGOUT:
                break;
            case REDIRECT:
                json.addProperty(Protocal.SERVER_HOST_NAME, msg.getHostnmae());
                json.addProperty(Protocal.SERVER_PORT, msg.getPort());
                break;
            case SERVER_ANNOUNCE:
                json.addProperty(Protocal.SERVER_ID, msg.getId());
                json.addProperty(Protocal.SERVER_LOAD, msg.getLoad());
                json.addProperty(Protocal.SERVER_HOST_NAME, msg.getHostnmae());
                json.addProperty(Protocal.SERVER_PORT, msg.getPort());
                break;
            case ACTIVITY_MESSAGE:
                json.addProperty(Protocal.USER_NAME, msg.getUsername());
                json.addProperty(Protocal.SECRET, msg.getSecret());
                json.addProperty(Protocal.ACTIVITY, msg.getActivity());
                break;
            case ACTIVITY_BROADCAST:
                json.addProperty(Protocal.ACTIVITY, msg.getActivity());
                break;
            case LOCK_ALLOWED:
                json.addProperty(Protocal.USER_NAME, msg.getUsername());
                json.addProperty(Protocal.SECRET, msg.getSecret());
                json.addProperty(Protocal.SERVER, msg.getServer());
                break;
            default:
                break;
        }
        return json.toString();
    }

}
