package com.server.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.server.beans.ServerInfo;
import com.server.beans.ServerValue;
import com.server.beans.UserInfo;

public class DataTable {
    private final Map<String, ServerValue> userTable = new ConcurrentHashMap<>();
    private final Map<String, ServerValue> serverTable = new ConcurrentHashMap<>();
    private static DataTable instance = null;

    public synchronized static DataTable getInstance() {
        if (instance == null) {
            instance = new DataTable();
        }
        return instance;
    }

    public boolean add(ServerValue value) {
        if (value == null) {
            return false;
        }
        if (value instanceof ServerInfo) {
            return put(serverTable, value);
        }
        else {
            return put(userTable, value);
        }
    }

    private boolean put(Map<String, ServerValue> map, ServerValue value) {
        if (map.containsKey(value)) {
            return false;
        }
        map.put(value.getKey(), value);
        return true;
    }

    public int getUserSize() {
        return userTable.size();
    }

    public void clear() {
        userTable.clear();
        serverTable.clear();
    }

    public List<UserInfo> getClients() {
        return new ConverMapToList<UserInfo>().toList(userTable);
    }

    public List<ServerInfo> getRemoteServers() {
        return new ConverMapToList<ServerInfo>().toList(serverTable);
    }

    private static class ConverMapToList<E> {
        @SuppressWarnings("unchecked")
        public List<E> toList(Map<String, ServerValue> map) {
            List<E> list = new ArrayList<>();
            synchronized (map) {
                Iterator<Entry<String, ServerValue>> it = map.entrySet()
                        .iterator();
                while (it.hasNext()) {
                    Entry<String, ServerValue> entry = it.next();
                    list.add((E) entry.getValue());
                }
            }
            return list;
        }
    }
}
