package com.protocal.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.protocal.connection.inter.Response;

public class ReadTask extends AbstractSocketTask {

    private BufferedReader reader = null;
    private Response response = null;

    public ReadTask(Socket socket, Connection connection) throws Exception {
        this(socket, null, connection);
    }

    public ReadTask(Socket socket, Response response, Connection connection)
            throws Exception {
        super(socket, connection);
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
                    close = response.process(message, connection);
                }
            }
            log.debug("connection closed to " + getSocketAddr()
                    + " by client initiatively.");
        }
        catch (Exception e) {
            log.error("connection " + getSocketAddr()
                    + " closed with exception: " + e);
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
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
