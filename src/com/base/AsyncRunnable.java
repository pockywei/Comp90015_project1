package com.base;

import javax.swing.SwingUtilities;

public abstract class AsyncRunnable extends BaseRunnable {

    private Callback callback;

    public AsyncRunnable setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public boolean runTask() throws Exception {
        handleMessage(onBackgroud());
        return true;
    }

    /**
     * true: success; false: failed
     * 
     * @return
     */
    protected abstract boolean onBackgroud() throws Exception;

    private void handleMessage(final boolean isSuccess) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (callback != null) {
                    callback.getResult(isSuccess);
                }
            }

        });
    }

    public interface Callback {
        public void getResult(boolean isSuccess);
    }
}
