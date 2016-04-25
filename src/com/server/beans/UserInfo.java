package com.server.beans;

public class UserInfo implements ServerValue, Comparable<UserInfo> {
    private String username;
    private String secret;

    public UserInfo(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    public String getUsername() {
        return username;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String getKey() {
        return username.toUpperCase();
    }

    @Override
    public String toString() {
        return username + " " + secret;
    }

    @Override
    public int compareTo(UserInfo another) {
        return getKey().equals(another.getKey()) ? 0 : 1;
    }
}
