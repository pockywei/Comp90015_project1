package com.client.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.base.BaseManager;
import com.client.UserSettings;
import com.client.core.inter.FrameUpdateListener;
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
        if (connection != null) {
            connection.close();
        }
        synchronized (frameList) {
            frameList.clear();
        }
    }

    @Override
    public boolean runTask() throws Exception {
        // do nothing...
        // Update solution may be that adds a heart-beat mechanism to keep the
        // socket connection with servers.
        return false;
    }

    public Connection createConnection() throws Exception {
        return connection = new Connection(
                new Socket(UserSettings.getRemoteHost(),
                        UserSettings.getRemotePort()),
                new ClientResponse());
    }

    public Connection getConnection() throws Exception {
        if (connection == null) {
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
}
