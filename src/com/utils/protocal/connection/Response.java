package com.utils.protocal.connection;

import java.io.IOException;
import java.net.Socket;

import com.server.core.ServerImpl;

public class Response extends Connection {

    public Response(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void runTask() throws Exception {
        listening();
    }

    /**
     * The read stream will be blocked until the socket time out.
     * 
     */
    protected void listening() {
        String message = null;
        boolean close = false;
        try {
            while (!close && ((message = read()) != null)) {
                close = process(message);
            }
            log.debug("connection closed to " + getSocketAddr());
        }
        catch (Exception e) {
            log.error("connection " + getSocketAddr()
                    + " closed with exception: " + e);
        }
        finally {
            close();
        }
    }

    @Override
    protected boolean process(String json) throws Exception {
        return false;
    }
    
    @Override
    public void close() {
        super.close();
        ServerImpl.getInstance().removeConnection(this);
    }

}
