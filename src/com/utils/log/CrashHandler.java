package com.utils.log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import com.base.BaseManager;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final Log log = Log.getInstance();
    private static CrashHandler instance = null;
    private List<BaseManager> managerList;
    private boolean isExit = false;
    private static final String OUTPUT_CRASH = "System shutdown by the exception!"
            + FileUtils.NEW_LINE;

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
        exit(-1);
    }

    private void exit(int arg) {
        isExit = true;
        // Received Exception, clear all system resource.
        synchronized (managerList) {
            for (BaseManager ba : managerList) {
                ba.clear();
            }
            managerList.clear();
        }
        log.debug(String.format("the system has been terminated by [%s]",
                arg == 0 ? "Normal State" : "Error State"));
        System.exit(arg);
    }

    public void exit() {
        exit(0);
    }

    public void errorExit() {
        exit(-1);
    }
}
