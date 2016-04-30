package com.protocal.connection;

import java.io.PrintWriter;
import java.net.Socket;

import com.base.EmptyRunnable;
import com.utils.UtilHelper;

public class WriteTask extends AbstractSocketTask {

    private PrintWriter outwriter = null;
    private String request = null;

    public WriteTask(Socket socket, Connection connection)
            throws Exception {
        super(socket, connection);
        this.outwriter = new PrintWriter(getOutputStream(), true);
        // start with a empty runnable because the write task will not handle
        // the first runnable.
        start(new EmptyRunnable());
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
                if (connection != null) {
                    connection.close();
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
