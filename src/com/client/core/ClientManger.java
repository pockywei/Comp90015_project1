package com.client.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.base.BaseManager;
import com.client.UserSettings;
import com.client.core.inter.FrameUpdateListener;
import com.client.core.request.ActivityMessage;
import com.client.core.request.LoginRequest;
import com.client.core.request.LogoutRequest;
import com.client.core.request.RegisterRequest;
import com.protocal.Command;
import com.protocal.connection.Connection;

public class ClientManger extends BaseManager {

    private static ClientManger instance = null;
    private Connection connection = null;
    private List<FrameUpdateListener> frameList = new ArrayList<>();

    public synchronized static ClientManger getInstance() {
        if (instance == null) {
            instance = new ClientManger();
        }
        return instance;
    }

    public ClientManger() {
        start();
    }

    public void addUIListener(FrameUpdateListener frame) {
        synchronized (frameList) {
            frameList.add(frame);
        }
    }

    public void removeUIListener(FrameUpdateListener frame) {
        synchronized (frameList) {
            frameList.remove(frame);
        }
    }

    @Override
    public void clear() {
        stop();
        // remove user's info.
        UserSettings.logoutUser();
        if (connection != null) {
            connection.close();
            connection = null;
        }
        synchronized (frameList) {
            frameList.clear();
        }
    }

    @Override
    public boolean runTask() throws Exception {
        // do nothing...but keep it alive. Once the system is finished, it will
        // be stopped.
        // TODO Update solution may be that adds a heart-beat mechanism to keep
        // the socket connection with servers.
        return true;
    }

    private Connection createConnection() throws Exception {
        if (connection != null) {
            connection.close();
        }
        return connection = new Connection(
                new Socket(UserSettings.getRemoteHost(),
                        UserSettings.getRemotePort()),
                new ClientResponse());
    }

    public Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            if (!stop) {
                return createConnection();
            }
        }
        return connection;
    }

    /**
     * Notify updates to all frame by thread-safe way.
     * 
     * @param com
     * @param info
     */
    public void notifyFrameSuccess(final Command com, final String info) {
        synchronized (frameList) {
            for (final FrameUpdateListener frame : frameList) {

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        frame.actionSuccess(com, info);
                    }
                });
            }
        }
    }

    /**
     * Notify updates to all frame by thread-safe way.
     * 
     * @param info
     */
    public void notifyFrameFailed(final String info) {
        synchronized (frameList) {
            for (final FrameUpdateListener frame : frameList) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        frame.actionFailed(info);
                    }
                });
            }
        }
    }

    public void sendLoginRequest() throws Exception {
        // new a connection.
        new LoginRequest(createConnection(), UserSettings.getUsername(),
                UserSettings.getSecret()).request();
    }

    public void sendRegisterRequest(String username, String secret)
            throws Exception {
        // new a connnection.
        new RegisterRequest(createConnection(), username, secret).request();
    }

    public void sendActivityMessage(String message) throws Exception {
        new ActivityMessage(getConnection(), UserSettings.getUsername(),
                UserSettings.getSecret(), message).request();
    }

    public void sendLogoutRequest() throws Exception {
        // logout, clear all info.
        if (new LogoutRequest(getConnection()).request()) {
            // wait for 1-2s, then close the connection.
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}
