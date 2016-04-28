package test;

import com.base.BaseRunnable;

public class TestLooper extends BaseRunnable {

    private String message;

    public TestLooper(String message) {
        this.message = message;
    }

    @Override
    public boolean runTask() throws Exception {
        System.out.println(
                Thread.currentThread().getName() + " running once. " + message);
        return true;
    }

    public void sendMsgAgain(String msg) {
        post(new TestLooper(msg));
    }

}
