package test;

import java.net.Socket;

import com.client.core.request.ActivityMessage;
import com.protocal.Protocal;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.Response;

public class InterfaceTest {

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("sunrise.cis.unimelb.edu.au", 5123);
        Connection c = new Connection(s, new SimpleResponse());
        if (new ActivityMessage(c, "anonymous", "", "Hi all.")
                .request()) {
            System.out.println("success");
        }

        Thread.sleep(5000);
        c.close();
    }
}

class SimpleResponse implements Response {

    @Override
    public boolean process(String json, Connection connection)
            throws Exception {
        System.out.println(json);
        return false;
    }

}
