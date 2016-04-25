package com.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.utils.log.Log;

public abstract class BaseFrame extends JFrame implements ActionListener {
    protected static final Log log = Log.getInstance();

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
