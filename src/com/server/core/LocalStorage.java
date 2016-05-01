package com.server.core;

import java.util.ArrayList;
import java.util.List;

import com.beans.UserInfo;
import com.protocal.Protocal;
import com.utils.UtilHelper;

public class LocalStorage {
    private final List<UserInfo> userTable = new ArrayList<>();
    private static LocalStorage instance = null;

    public synchronized static LocalStorage getInstance() {
        if (instance == null) {
            instance = new LocalStorage();
        }
        return instance;
    }

    /**
     * true: add success false: add failed
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
     * true: login success false: secret does not match.
     * 
     * @param user
     * @return
     */
    public boolean loginCheck(UserInfo user) {
        if (isAnonymousUser(user)) {
            return true;
        }
        synchronized (userTable) {
            for (UserInfo u : userTable) {
                if (u.equals(user) && u.getSecret().equals(user.getSecret())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeUser(UserInfo user) {
        synchronized (userTable) {
            for (UserInfo u : userTable) {
                if (u.equals(user) && u.getSecret().equals(user.getSecret())) {
                    userTable.remove(u);
                    break;
                }
            }
        }
    }

    public void clear() {
        synchronized (userTable) {
            userTable.clear();
        }
    }

    public final List<UserInfo> getRegisteredUsers() {
        return userTable;
    }

    public boolean isAnonymousUser(UserInfo user) {
        return user.getUsername().equals(Protocal.ANONYMOUS)
                && UtilHelper.isEmptyStr(user.getSecret());
    }
}
