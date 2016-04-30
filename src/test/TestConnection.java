package test;

import java.net.Socket;

import com.protocal.connection.Connection;
import com.protocal.connection.inter.Response;

public class TestConnection {

    public static void main(String[] args) throws Exception {
        int n = 10;
        Socket s = new Socket("127.0.0.1", 9091);
        Connection c = new Connection(s, new iResponse());
        for (int i = 0; i < n; i++) {
            c.sendMessage("HI " + i);
            Thread.sleep(1000);
        }
    }

}

class iResponse implements Response {

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        System.out.println(json);
        if (json.equals("get HI 6")) {
            connection.sendMessage("Client special.");
        }
        else if (json.equals("get HI 9")) {
            // close connection and threads.
            return true;
        }
        return false;
    }

}