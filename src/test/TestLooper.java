package test;

import com.base.BaseLooper;

public class TestLooper extends BaseLooper {

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
