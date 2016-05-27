package com.client.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.base.AsyncRunnable;
import com.base.AsyncRunnable.Callback;
import com.base.BaseManager;
import com.client.UserSettings;
import com.client.core.inter.FrameUpdateListener;
import com.client.core.request.ActivityMessage;
import com.client.core.request.LoginRequest;
import com.client.core.request.LogoutRequest;
import com.client.core.request.RegisterRequest;
import com.protocal.Command;
import com.protocal.Protocal;
import com.protocal.connection.Connection;

public class ClientManger extends BaseManager implements FrameUpdateListener {

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
    public boolean runTask() throws Exception {
        // do nothing...but keep it alive. Once the system is finished, it will
        // be stopped.
        // TODO Update solution may be that adds a heart-beat mechanism to keep
        // the socket connection with servers.
        return true;
    }

    private Connection createConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            log.debug("waiting for creating connection to remote server: "
                    + UserSettings.getRemoteHost() + ":"
                    + UserSettings.getRemotePort());
            return connection = new Connection(
                    new Socket(UserSettings.getRemoteHost(),
                            UserSettings.getRemotePort()),
                    new ClientResponse(this));
        }
        catch (Exception e) {
            log.error("connection create failed " + e);
            actionFailed(Command.CONNECTION_ERROR, Protocal.CONNECTION_FAIL);
        }
        return null;
    }

    public Connection getConnection() {
        if (connection == null || connection.isClosed()) {
            if (!stop) {
                return createConnection();
            }
        }
        return connection;
    }

    public void sendLoginRequest(Callback callback) throws Exception {
        post(new AsyncRunnable() {

            @Override
            protected boolean onBackgroud() throws Exception {
                return new LoginRequest(createConnection(),
                        UserSettings.getUsername(), UserSettings.getSecret())
                                .request();
            }
        }.setCallback(callback));
    }

    public void sendRegisterRequest(final String username, final String secret,
            Callback callback) throws Exception {
        post(new AsyncRunnable() {

            @Override
            protected boolean onBackgroud() {
                return new RegisterRequest(createConnection(), username, secret)
                        .request();
            }
        }.setCallback(callback));
    }

    public void sendActivityMessage(final String message) throws Exception {
        post(new AsyncRunnable() {

            @Override
            protected boolean onBackgroud() {
                return new ActivityMessage(getConnection(),
                        UserSettings.getUsername(), UserSettings.getSecret(),
                        message).request();
            }
        });
    }

    public void sendLogoutRequest() throws Exception {
        post(new AsyncRunnable() {

            @Override
            protected boolean onBackgroud() throws Exception {
                // logout, clear all info.
                if (new LogoutRequest(getConnection()).request()) {
                    // wait for 1-2s, then close the connection, because the
                    // server may not reply for logout command.
                    Thread.sleep(1000);
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void actionSuccess(final Command com, final String info, final Object o) {
        synchronized (frameList) {
            for (final FrameUpdateListener frame : frameList) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        frame.actionSuccess(com, info, o);
                    }
                });
            }
        }
    }

    @Override
    public void actionFailed(final Command com, final String info) {
        synchronized (frameList) {
            for (final FrameUpdateListener frame : frameList) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        frame.actionFailed(com, info);
                    }
                });
            }
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
}
