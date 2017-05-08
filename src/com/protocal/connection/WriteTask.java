package com.protocal.connection;

import java.io.PrintWriter;
import java.net.Socket;

import com.base.BaseLooper;
import com.utils.UtilHelper;

public class WriteTask extends AbstractSocketTask {

    private PrintWriter outwriter = null;

    public WriteTask(Socket socket, Connection connection) throws Exception {
        super(socket, connection);
        this.outwriter = new PrintWriter(getOutputStream(), true);
        start();
    }

    @Override
    public boolean runTask() throws Exception {
        // do nothing, keep the write task as a message queue.
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
        post(new SendTask(request));
    }

    private class SendTask extends BaseLooper {

        private String request;

        public SendTask(String request) {
            this.request = request;
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

    }
}
