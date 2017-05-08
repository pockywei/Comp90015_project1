package com.base;

import com.utils.log.CrashHandler;

public abstract class BaseManager extends BaseRunnable {

    public BaseManager() {
        CrashHandler.getInstance().addManager(this);
    }

    public abstract void clear();
}
