package com.server.core;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Util;

import com.beans.ServerInfo;
import com.beans.UserInfo;
import com.protocal.Protocal;
import com.utils.UtilHelper;

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

    /**
     * true: not register on local false: has registered on local
     * 
     * @param user
     * @return
     */
    public boolean addUser(UserInfo user) {
        // anonymous will be allowed to send message, but can not add into the
        // user table.
        if (isAnonymousUser(user)) {
            return false;
        }
        synchronized (userTable) {
            if (userTable.contains(user)) {
                return false;
            }
            userTable.add(user);
        }
        return true;
    }

    /**
     * true: has registered on local false: not register on local
     * 
     * @param user
     * @return
     */
    public boolean hasUser(UserInfo user) {
        if (isAnonymousUser(user)) {
            return true;
        }
        synchronized (userTable) {
            return userTable.contains(user);
        }
    }

    /**
     * true: the server has been authenticated
     * 
     * @param sever
     * @return
     */
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

    public boolean isAnonymousUser(UserInfo user) {
        return user.getUsername().equals(Protocal.ANONYMOUS)
                && UtilHelper.isEmptyStr(user.getSecret());
    }
}
