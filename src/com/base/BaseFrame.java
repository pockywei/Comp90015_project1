package com.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.client.core.ClientManger;
import com.client.core.inter.FrameUpdateListener;
import com.utils.log.Log;

public abstract class BaseFrame extends JFrame
        implements ActionListener, FrameUpdateListener {

    protected static final Log log = Log.getInstance();

    public BaseFrame() {
        ClientManger.getInstance().addUIListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
