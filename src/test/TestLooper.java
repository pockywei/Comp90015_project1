package test;

import com.base.BaseRunnable;

public class TestLooper extends BaseRunnable {

    private String message;

    public TestLooper(String message) {
        isLoop = true;
        this.message = message;
    }

    @Override
    public void runTask() throws Exception {
        System.out.println(
                Thread.currentThread().getName() + " running once. " + message);
    }

    public void sendMsgAgain(String msg) {
        next(new TestLooper(msg));
    }

}
