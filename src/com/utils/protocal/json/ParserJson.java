package com.utils.protocal.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.utils.protocal.Command;
import com.utils.protocal.Message;
import com.utils.protocal.Protocal;

public class ParserJson {

    String json;

    public ParserJson(String json) {
        this.json = json;
    }

    public Message getMsg() throws Exception {
        Message msg = null;
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        if (!root.isJsonObject() || !root.has(Protocal.COMMAND)) {
            throw new Exception();
        }

        Command com = Command
                .getCommand(root.get(Protocal.COMMAND).getAsString());
        if (com == null) {
            throw new Exception();
        }
        msg = new Message(com);
        switch (com) {
            case LOGIN_FAILED:
            case LOGIN_SUCCESS:
            case AUTHENTICATION_FAIL:
            case REGISTER_FAILED:
            case INVALID_MESSAGE:
            case REGISTER_SUCCESS:
                if (!root.has(Protocal.INFO)) {
                    throw new Exception();
                }
                msg.setInfo(root.get(Protocal.INFO).getAsString());
                break;
            case AUTHENTICATE:
                if (!root.has(Protocal.SECRET)) {
                    throw new Exception();
                }
                msg.setSecret(root.get(Protocal.SECRET).getAsString());
                break;
            case LOCK_REQUEST:
            case REGISTER:
            case LOGIN:
            case LOCK_DENIED:
                if (!root.has(Protocal.USER_NAME)
                        || !root.has(Protocal.SECRET)) {
                    throw new Exception();
                }
                msg.setUsername(root.get(Protocal.USER_NAME).getAsString());
                msg.setSecret(root.get(Protocal.SECRET).getAsString());
                break;
            case LOGOUT:
                break;
            case REDIRECT:
                if (!root.has(Protocal.SERVER_HOST_NAME)
                        || !root.has(Protocal.SERVER_PORT)) {
                    throw new Exception();
                }
                msg.setHostnmae(
                        root.get(Protocal.SERVER_HOST_NAME).getAsString());
                msg.setPort(root.get(Protocal.SERVER_PORT).getAsInt());
                break;
            case SERVER_ANNOUNCE:
                if (!root.has(Protocal.SERVER_ID)
                        || !root.has(Protocal.SERVER_LOAD)
                        || !root.has(Protocal.SERVER_HOST_NAME)
                        || !root.has(Protocal.SERVER_PORT)) {
                    throw new Exception();
                }
                msg.setHostnmae(
                        root.get(Protocal.SERVER_HOST_NAME).getAsString());
                msg.setPort(root.get(Protocal.SERVER_PORT).getAsInt());
                msg.setId(root.get(Protocal.SERVER_ID).getAsString());
                msg.setLoad(root.get(Protocal.SERVER_LOAD).getAsInt());
                break;
            case ACTIVITY_MESSAGE:
                if (!root.has(Protocal.USER_NAME) || !root.has(Protocal.SECRET)
                        || !root.has(Protocal.ACTIVITY)) {
                    throw new Exception();
                }
                msg.setUsername(root.get(Protocal.USER_NAME).getAsString());
                msg.setSecret(root.get(Protocal.SECRET).getAsString());
                msg.setActivity(root.get(Protocal.ACTIVITY).getAsString());
                break;
            case ACTIVITY_BROADCAST:
                if (!root.has(Protocal.ACTIVITY)) {
                    throw new Exception();
                }
                msg.setActivity(root.get(Protocal.ACTIVITY).getAsString());
                break;
            case LOCK_ALLOWED:
                if (!root.has(Protocal.USER_NAME) || !root.has(Protocal.SECRET)
                        || !root.has(Protocal.SERVER)) {
                    throw new Exception();
                }
                msg.setUsername(root.get(Protocal.USER_NAME).getAsString());
                msg.setSecret(root.get(Protocal.SECRET).getAsString());
                msg.setSecret(root.get(Protocal.SERVER).getAsString());
                break;
            default:
                break;
        }

        return msg;
    }
}
