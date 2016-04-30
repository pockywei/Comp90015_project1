package com.server.core;

import java.util.ArrayList;
import java.util.List;

import com.beans.ServerInfo;
import com.beans.UserInfo;

public class LocalStorage {
    private final List<UserInfo> userTable = new ArrayList<>();
    private final List<ServerInfo> serverTable = new ArrayList<>();
    private static LocalStorage instance = null;

    public synchronized static LocalStorage getInstance() {
        if (instance == null) {
            instance = new LocalStorage();
        }
        return instance;
    }

    public boolean addUser(UserInfo user) {
        synchronized (userTable) {
            if (userTable.contains(user)) {
                return false;
            }
            userTable.add(user);
        }
        return true;
    }

    public boolean addServer(ServerInfo sever) {
        synchronized (serverTable) {
            if (serverTable.contains(sever)) {
                return false;
            }
            serverTable.add(sever);
        }
        return true;
    }

    public void clear() {
        synchronized (userTable) {
            userTable.clear();
        }
        synchronized (serverTable) {
            serverTable.clear();
        }
    }

    public final List<UserInfo> getRegisteredUsers() {
        return userTable;
    }

    public final List<ServerInfo> getAdjacentServers() {
        return serverTable;
    }

    public void updateServerInfo(ServerInfo update) {
        synchronized (serverTable) {
            for (ServerInfo server : serverTable) {
                if (server.equals(update)) {
                    server.setLoad(update.getLoad());
                    server.setId(update.getId());
                }
            }
        }
    }
}
