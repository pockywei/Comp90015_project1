package com.base;

import java.util.concurrent.LinkedBlockingQueue;

import com.utils.log.Log;

public abstract class BaseRunnable implements Runnable {
    protected static final Log log = Log.getInstance();
    protected boolean stop = true;
    /**
     * true: the thread will keep doing runnable by the next() method until
     * stop.
     * 
     * false: only run once.
     */
    protected boolean isLoop = false;
    private LinkedBlockingQueue<BaseRunnable> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        stop = false;
        try {
            // Start doing first task.
            runTask();
            // e.g. if connection should not be closed, each connection can set
            // (isLoop = true) to keep the connection thread alive.
            while (isLoop && !stop) {
                queue.take().runTask();
            }
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
        log.debug("Task has been stopped.");
    }

    public void start() {
        // Starting a thread or adding this runnable into a thread-pool.
        new Thread(this).start();
    }

    protected void next(BaseRunnable runnable) {
        queue.offer(runnable);
    }
}
