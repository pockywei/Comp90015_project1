package com.protocal.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.protocal.connection.inter.ConnectionListener;
import com.protocal.connection.inter.Response;

public class ReadTask extends AbstractSocketTask {

    private BufferedReader reader = null;
    private Response response = null;

    public ReadTask(Socket socket, ConnectionListener closeListener)
            throws Exception {
        this(socket, null, closeListener);
    }

    public ReadTask(Socket socket, Response response,
            ConnectionListener closeListener) throws Exception {
        super(socket, closeListener);
        this.reader = new BufferedReader(
                new InputStreamReader(getInputStream()));
        this.response = response;
        start();
    }

    @Override
    public boolean runTask() throws Exception {
        listening();
        return false;
    }

    /**
     * The read stream will be blocked until the socket time out or close.
     * 
     */
    protected void listening() {
        String message = null;
        boolean close = false;
        try {
            while (!close && ((message = reader.readLine()) != null)) {
                if (response != null) {
                    close = response.process(message, connectionListener);
                }
            }
            log.debug("connection closed to " + getSocketAddr());
        }
        catch (Exception e) {
            log.error("connection " + getSocketAddr()
                    + " closed with exception: " + e);
        }
        finally {
            try {
                if (connectionListener != null) {
                    connectionListener.close();
                }
            }
            catch (Exception e) {
                log.error("connection close exception." + e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (reader != null) {
            reader.close();
        }
        stop();
    }
}
