package com.base;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.client.core.ClientManger;
import com.client.core.inter.FrameUpdateListener;
import com.client.ui.ActionDialog;
import com.utils.log.CrashHandler;
import com.utils.log.Log;

public abstract class BaseFrame extends JFrame
        implements FrameUpdateListener, ActionListener {

    private static final long serialVersionUID = 1L;
    private static final String TITLE = "ActivityStreamer Text I/O by Eyebrow";
    protected static final Log log = Log.getInstance();
    private ActionDialog progressBar = new ActionDialog(BaseFrame.this);;

    public abstract void initView();

    public BaseFrame() {
        setTitle(TITLE);
        // Initialize views.
        initView();
        // set the frame to the center of the current srceen.
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2,
                (dim.height - abounds.height) / 2);

        setResizable(false);
        setVisible(true);
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

    public void nextFrame(Class<? extends BaseFrame> frame) {
        close();
        try {
            frame.newInstance();
        }
        catch (Exception e) {
            log.error("start frame failed from " + this.getName() + " to "
                    + frame.getName());
            CrashHandler.getInstance().errorExit();
        }
    }

    public void close() {
        if (progressBar != null) {
            progressBar.close();
        }
        setVisible(false);
        dispose();
    }

    public void showProgress() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (!progressBar.isVisible()) {
                    log.info("show progress bar");
                    progressBar.setVisible(true);
                }
            }
        });
    }

    public void closeProgress() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisible(false);
                log.info("close progress bar");
            }
        });

    }
}
