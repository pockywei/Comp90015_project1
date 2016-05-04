package com.base;

import com.client.core.inter.FrameUpdateListener;
import com.protocal.Command;

public abstract class AsyncRunnable extends BaseRunnable
        implements FrameUpdateListener {

    @Override
    public void actionSuccess(Command com, String info) {

    }

    @Override
    public void actionFailed(Command com, String info) {

    }

    @Override
    public boolean runTask() throws Exception {
        return false;
    }

    @Override
    public void start() {
        preTask();
        super.start();
    }

    /**
     * Provided for showing progress dialog or something that indicates user
     * doing tasks.
     * 
     */
    protected abstract void preTask();
}
