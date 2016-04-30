package com.beans;

public class UserInfo implements Record {

    private String username;
    private String secret;

    public UserInfo() {
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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
    public boolean equals(Object o) {
        return getKey().equals(((Record) o).getKey());
    }
}
