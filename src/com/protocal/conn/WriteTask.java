package com.protocal.conn;

import java.io.PrintWriter;
import java.net.Socket;

import com.protocal.connection.inter.ConnectionListener;
import com.utils.UtilHelper;

public class WriteTask extends AbstractSocketTask {

    private PrintWriter outwriter = null;
    private String request = null;

    public WriteTask(Socket socket, ConnectionListener closeListener)
            throws Exception {
        super(socket, closeListener);
        this.outwriter = new PrintWriter(getOutputStream(), true);
        start();
    }

    @Override
    public boolean runTask() throws Exception {
        if (!UtilHelper.isEmptyStr(request)) {
            log.debug("request json: " + request);
            try {
                outwriter.println(request);
                outwriter.flush();
            }
            catch (Exception e) {
                log.error("write exception, closing request. " + e);
                if (connectionListener != null) {
                    connectionListener.close();
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
        stop();
    }

    /**
     * Send message
     * 
     * @param com
     * @param activity
     * @return true: send success; false: send failed as connection has been
     *         closed.
     */
    public void sendMessage(String request) {
        this.request = request;
        post(this);
    }

}
