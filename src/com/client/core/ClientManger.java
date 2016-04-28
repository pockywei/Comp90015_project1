package com.client.core;

import com.base.BaseManager;

public class ClientManger extends BaseManager {

    private static ClientManger instance = null;

    public synchronized static ClientManger getInstance() {
        if (instance == null) {
            instance = new ClientManger();
        }
        return instance;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean runTask() throws Exception {
        
        return false;
    }

}
