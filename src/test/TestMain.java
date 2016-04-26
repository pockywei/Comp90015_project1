package test;

public class TestMain {

    public static void main(String[] args) {
        TestLooper looper = new TestLooper("");
        looper.start();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            looper.sendMsgAgain(i + " zac");
        }
    }
}
