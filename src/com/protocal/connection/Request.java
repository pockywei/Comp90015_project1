package com.protocal.connection;

import java.io.IOException;
import java.net.Socket;

import com.protocal.Command;
import com.protocal.Message;
import com.protocal.json.JsonBuilder;

public abstract class Request extends Connection {

    protected String activity;

    public Request(Socket socket, Command com) throws IOException {
        super(socket, com);
        // As request would be continual to send activity message or announce.
        isLoop = true;
    }

    public Request(Socket socket, Command com, String activity)
            throws IOException {
        this(socket, com);
        this.activity = activity;
    }

    @Override
    public void runTask() throws Exception {
        Message msg = getSendMsg();
        if (msg == null) {
            log.error("sending message error by command " + com);
            throw new Exception();
        }
        String json = new JsonBuilder(msg).getJson(com);
        log.debug("request json: " + json + " by " + com);
        try {
            write(json);
            if (process(read())) {
                close();
            }
        }
        catch (Exception e) {
            log.error(
                    "write or read or parse exception, closing request. " + e);
            close();
        }
    }

    protected abstract Message getSendMsg();

    /**
     * For next request.
     * 
     * @param com
     * @param activity
     * @return true: send success; false: send failed as connection has been
     *         closed.
     */
    public abstract boolean nextMessage(Command com, String activity);
}
