package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.client.UserSettings;
import com.protocal.Command;
import com.protocal.Message;
import com.protocal.json.JsonBuilder;
import com.protocal.json.ParserJson;
import com.utils.UtilHelper;
import com.utils.log.Log;

public class ClientTest {

    private static final Log log = Log.getInstance();

    public static void main(String[] args) throws Exception {
        String addr = "sunrise.cis.unimelb.edu.au";
        Socket s = new Socket(addr, 5123);
        String address = s.getInetAddress() + ":" + s.getPort();
        System.out.println(address);

        String user = "zac";
        String secret = "123456";

        // write
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        // read
        BufferedReader input = new BufferedReader(
                new InputStreamReader(s.getInputStream()));

        String json = "";
        String back = "";
        Message msg = null;

        // Register
        json = new JsonBuilder(
                Message.getUserMsg(user, secret, Command.REGISTER))
                        .getJson(Command.REGISTER);
        log.info("request: " + json);

        out.write(json);
        out.flush();

        // Read
        back = input.readLine();
        log.info("back : " + back);
        msg = new ParserJson(back).getMsg();

        switch (msg.getCommand()) {
            case REGISTER_SUCCESS:
                // Login
                json = new JsonBuilder(
                        Message.getUserMsg(user, secret, Command.LOGIN))
                                .getJson(Command.LOGIN);
                log.info("request login: " + json);

                out.write(json);
                out.flush();

                // Read
                back = input.readLine();
                log.info("back : " + back);

                msg = new ParserJson(back).getMsg();
                switch (msg.getCommand()) {
                    case LOGIN_SUCCESS:
                        // Send an Activity Message
                        json = new JsonBuilder(
                                Message.getActivityMsg(user, secret,
                                        "Hi everyone! Please send back message to me!",
                                        Command.ACTIVITY_MESSAGE))
                                                .getJson(Command.LOGIN);

                        log.info("request message: " + json);

                        out.write(json);
                        out.flush();

                        back = input.readLine();
                        log.info("back : " + back);

                        // Listen message
                        while ((back = input.readLine()) != null) {
                            log.info("back : " + back);
                        }
                        break;
                    case LOGIN_FAILED:
                        log.info("LOGIN_FAILED: " + msg.getInfo());
                        break;
                    default:
                        break;
                }

                break;
            case REGISTER_FAILED:
                log.info("REGISTER_FAILED: " + msg.getInfo());
                break;
            default:
                break;
        }

    }
}
