package com.protocal.conn;

import java.io.PrintWriter;
import java.net.Socket;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.connection.inter.ConnectionListener;
import com.protocal.json.JsonBuilder;

public class Writer extends SocketTask {

    private PrintWriter outwriter = null;
    private Request request = null;

    public Writer(Socket socket, ConnectionListener closeListener) throws Exception {
        super(socket, closeListener);
        outwriter = new PrintWriter(getOutputStream(), true);
        start();
    }

    @Override
    public boolean runTask() throws Exception {
        if (request != null) {
            Message msg = request.getRequestMessage();
            if (msg == null) {
                log.error("sending message error.");
                throw new Exception();
            }
            String json = new JsonBuilder(msg).getJson(msg.getCommand());
            log.debug("request json: " + json + " by " + msg.getCommand());
            try {
                outwriter.println(json);
                outwriter.flush();
            }
            catch (Exception e) {
                log.error(
                        "write exception, closing request. " + e);
                if (closeListener != null) {
                    closeListener.close();
                }
            }
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (outwriter != null) {
            outwriter.close();
        }
    }
    
    /**
     * For next request.
     * 
     * @param com
     * @param activity
     * @return true: send success; false: send failed as connection has been
     *         closed.
     */
    public boolean nextMessage(Command com, String activity) {
        
        return true;
    }

}
