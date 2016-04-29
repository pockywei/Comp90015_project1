package com.base;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.client.core.ClientManger;
import com.client.core.inter.FrameUpdateListener;
import com.utils.log.CrashHandler;
import com.utils.log.Log;

public abstract class BaseFrame extends JFrame
        implements FrameUpdateListener, ActionListener {

    private static final long serialVersionUID = 1L;
    private static final String TITLE = "ActivityStreamer Text I/O";
    protected static final Log log = Log.getInstance();

    public BaseFrame() {
        setTitle(TITLE);
        ClientManger.getInstance().addUIListener(this);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CrashHandler.getInstance().exit();
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        ClientManger.getInstance().removeUIListener(this);
    }

}
