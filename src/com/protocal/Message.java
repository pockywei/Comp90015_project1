package com.protocal;

import com.server.beans.ServerInfo;
import com.server.beans.UserInfo;

public class Message {

    private Command com;
    private String info = "";
    private String secret = "";
    private String username = "";
    private String hostnmae = "";
    private int port = 0;
    private String activity = "";
    private int load = 0;
    private String id = "";
    private String server = "";

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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
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
                + hostnmae + " " + port + " " + activity + " " + load + " " + id
                + " " + server;
    }

    public UserInfo toUserInfo() {
        return new UserInfo(getUsername(), getSecret());
    }

    public ServerInfo toServerInfo() {
        return new ServerInfo(getPort(), getHostnmae(), getSecret(), getLoad());
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
            String activity, Command com) {
        Message msg = new Message(com);
        msg.setSecret(secret);
        msg.setUsername(username);
        msg.setActivity(activity);
        return msg;
    }

    public static Message getBroadcastMsg(String activity, Command com) {
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
}
