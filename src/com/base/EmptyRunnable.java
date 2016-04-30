package com.base;

public class EmptyRunnable extends BaseRunnable {

    @Override
    public boolean runTask() throws Exception {
        // do nothing, provide for write task that can be able to skip the first
        // runnable as a empty runnable, but keep the task alive.
        return true;
    }

}
