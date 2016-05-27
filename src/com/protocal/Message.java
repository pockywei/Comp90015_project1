package com.protocal;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.google.gson.JsonObject;
import com.utils.UtilHelper;

public class Message {

    private Command com;
    private String info = "";
    private String secret = "";
    private String username = "";
    private String hostnmae = "";
    private int port = 0;
    private int load = 0;
    private String id = "";
    private String server = "";
    private Activity activity = null;

    public Message() {
    }

    public Message(Command com) {
        this.com = com;
    }

    public Command getCommand() {
        return com;
    }

    public void setCommand(Command com) {
        this.com = com;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHostnmae() {
        return hostnmae;
    }

    public void setHostnmae(String hostname) {
        this.hostnmae = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return com.name() + " " + info + " " + secret + " " + username + " "
                + hostnmae + " " + port + " " + toActivity() + " " + load + " "
                + id + " " + server;
    }

    private String toActivity() {
        if (activity == null) {
            return "";
        }
        return activity.toString();
    }

    public UserInfo toUserInfo() {
        return new UserInfo(getUsername(), getSecret());
    }

    public ServerInfo toServerInfo() {
        return new ServerInfo(getId(), getPort(), getHostnmae(), getSecret(),
                getLoad());
    }

    public JsonObject getActivityMsgJson() {
        // if the input message is a json object, then send it directly.
        // otherwise, build a new json object with the field "message".
        JsonObject activity = UtilHelper
                .isJsonObject(getActivity().getMessage());
        if (activity != null) {
            return activity;
        }
        activity = new JsonObject();
        activity.addProperty(Protocal.ACTIVITY_MESSAGE,
                getActivity().getMessage());
        return activity;
    }

    public JsonObject getActivityCastJson() {
        JsonObject activity = UtilHelper
                .isJsonObject(getActivity().getMessage());
        // if the message is a json object, then add a field to indicate the
        // user name. otherwise, build a new json object with the field
        // "message".
        if (activity != null) {
            activity.addProperty(Protocal.ACTIVITY_SENDER,
                    getActivity().getUsername());
            return activity;
        }
        activity = new JsonObject();
        activity.addProperty(Protocal.ACTIVITY_SENDER,
                getActivity().getUsername());
        activity.addProperty(Protocal.ACTIVITY_MESSAGE,
                getActivity().getMessage());
        return activity;
    }

    /**
     * Used for LOGIN_FAILED: LOGIN_SUCCESS: AUTHENTICATION_FAIL:
     * REGISTER_FAILED: INVALID_MESSAGE: REGISTER_SUCCESS:
     * 
     * @param info
     * @param com
     * @return
     */
    public static Message getResponseMsg(String info, Command com) {
        Message msg = new Message(com);
        msg.setInfo(info);
        return msg;
    }

    public static Message getAuthenticateMsg(String secret, Command com) {
        Message msg = new Message(com);
        msg.setSecret(secret);
        return msg;
    }

    /**
     * Used for LOCK_REQUEST: REGISTER: LOGIN: LOCK_DENIED:
     * 
     * @param secret
     * @param com
     * @return
     */
    public static Message getUserMsg(String username, String secret,
            Command com) {
        Message msg = new Message(com);
        msg.setSecret(secret);
        msg.setUsername(username);
        return msg;
    }

    public static Message getRedirectMsg(String hostname, int port,
            Command com) {
        Message msg = new Message(com);
        msg.setPort(port);
        msg.setHostnmae(hostname);
        return msg;
    }

    public static Message getAnnounceMsg(String id, int load, String hostname,
            int port, Command com) {
        Message msg = new Message(com);
        msg.setPort(port);
        msg.setHostnmae(hostname);
        msg.setLoad(load);
        msg.setId(id);
        return msg;
    }

    public static Message getActivityMsg(String username, String secret,
            Activity activity, Command com) {
        Message msg = new Message(com);
        msg.setSecret(secret);
        msg.setUsername(username);
        msg.setActivity(activity);
        return msg;
    }

    public static Message getBroadcastMsg(Activity activity, Command com) {
        Message msg = new Message(com);
        msg.setActivity(activity);
        return msg;
    }

    public static Message getLockAllowedMsg(String username, String secret,
            String server, Command com) {
        Message msg = new Message(com);
        msg.setSecret(secret);
        msg.setUsername(username);
        msg.setServer(server);
        return msg;
    }

    /**
     * Build send activity message
     * 
     * @param message
     * @return
     */
    public static Activity getActivity(String message) {
        return getActivity(message, "");
    }

    /**
     * Build receive activity message
     * 
     * @param message
     * @param username
     * @return
     */
    public static Activity getActivity(String message, String username) {
        return new Activity().setMessage(message).setUsername(username);
    }
}
