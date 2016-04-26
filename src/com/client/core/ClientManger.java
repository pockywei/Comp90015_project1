package com.client.core;

import java.io.IOException;
import java.net.Socket;

import com.base.BaseManager;
import com.utils.protocal.Command;
import com.utils.protocal.connection.Connection;
import com.utils.protocal.connection.inter.RequestListener;

public class ClientManger extends BaseManager implements RequestListener {

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
    public void runTask() throws Exception {

    }

    @Override
    public Connection request(Socket s, Command com) throws IOException {
        return null;
    }

}
