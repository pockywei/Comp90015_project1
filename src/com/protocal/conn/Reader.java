package com.protocal.conn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.protocal.connection.inter.ConnectionListener;

public class Reader extends SocketTask {

    private BufferedReader reader = null;
    private Response response = null;

    public Reader(Socket socket, ConnectionListener closeListener) throws Exception {
        super(socket, closeListener);
        reader = new BufferedReader(new InputStreamReader(getInputStream()));
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
                    close = response.process(message);
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
                if (closeListener != null) {
                    closeListener.close();
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
    }

}
