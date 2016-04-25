package com.base;

import com.utils.log.Log;

public abstract class BaseRunnable implements Runnable {
    protected static final Log log = Log.getInstance();
    protected boolean stop = true;

    @Override
    public void run() {
        stop = false;
        try {
            runTask();
        }
        catch (Exception e) {
            log.error("Task has been terminated by the exception: " + e);
        }
        finally {
            stop();
        }
    }

    public abstract void runTask() throws Exception;

    public boolean isRunning() {
        return !stop;
    }

    public void stop() {
        stop = true;
    }

    public void start() {
        // Starting a thread or adding this runnable into a thread-pool.
        new Thread(this).start();
    }
}
