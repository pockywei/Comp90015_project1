package com.utils.log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import com.base.BaseManager;
import com.base.BaseRunnable;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final Log log = Log.getInstance();
    private static CrashHandler instance = null;
    private static final String OUTPUT_CRASH = "System shutdown by the exception!"
            + FileUtils.NEW_LINE;
    private List<BaseManager> managerList;
    private boolean isExit = false;
    private CrashListener crashListener;

    public void setCrashListener(CrashListener crashListener) {
        this.crashListener = crashListener;
    }

    public synchronized static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public CrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        managerList = new ArrayList<>();
    }

    public void addManager(BaseManager ba) {
        if (ba == null) {
            return;
        }
        synchronized (managerList) {
            if (!isExit) {
                managerList.add(ba);
            }
        }
        testCrash();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringBuilder br = new StringBuilder(OUTPUT_CRASH);
        StackTraceElement[] stack = ex.getStackTrace();
        br.append(thread.getName() + "-" + ex + FileUtils.NEW_LINE);
        for (StackTraceElement e : stack) {
            br.append(e.toString() + FileUtils.NEW_LINE);
        }
        log.error(br.toString());
        if (crashListener != null) {
            crashListener.crash();
        }
        else {
            errorExit();
        }
    }

    /**
     * System Exit Task.
     * 
     * @param arg
     */
    private void exit(final int arg) {
        new BaseRunnable() {

            @Override
            public boolean runTask() throws Exception {
                isExit = true;
                // wait for sending crash broadcast.
                Thread.sleep(3000);
                // Received Exception, clear all system resource.
                synchronized (managerList) {
                    for (BaseManager ba : managerList) {
                        ba.clear();
                    }
                    managerList.clear();
                }
                log.debug(
                        String.format("the system has been terminated by [%s]",
                                arg == 0 ? "Normal State" : "Error State"));
                System.exit(arg);
                return false;
            }
        }.start();
    }

    public void exit() {
        exit(0);
    }

    public void errorExit() {
        exit(-1);
    }

    private void testCrash() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    log.debug("test server crash-----------------------");
                    throw new NullPointerException();
                }
                catch (InterruptedException e) {

                }
            }
        }).start();
    }
}
