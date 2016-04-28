package test;

public class TestLooperMain {

    public static void main(String[] args) {
        TestLooper looper = new TestLooper("");
        looper.start();
        
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            looper.sendMsgAgain(i + " zac");
        }
        looper.stop();
    }
}
