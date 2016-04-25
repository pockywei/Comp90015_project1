package com.utils.protocal.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.base.BaseRunnable;
import com.server.core.ServerImpl;
import com.utils.UtilHelper;
import com.utils.protocal.Command;

public abstract class Connection extends BaseRunnable {

    private BufferedReader reader = null;;
    private PrintWriter outwriter = null;
    private Socket socket;
    protected Command com = null;

    public Connection(Socket socket) throws IOException {
        this(socket, null);
    }

    public Connection(Socket socket, Command com) throws IOException {
        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        outwriter = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.com = com;
        start();
    }

    /**
     * The read stream will be blocked until the socket time out.
     * 
     */
    protected void read() {
        String message = null;
        boolean close = false;
        try {
            while (!close && ((message = reader.readLine()) != null)) {
                close = process(message);
            }
            log.debug("connection closed to " + getSocketAddr());
        }
        catch (Exception e) {
            log.error("connection " + getSocketAddr()
                    + " closed with exception: " + e);
        }
        finally {
            ServerImpl.getInstance().removeConnection(this);
            close();
        }
    }

    /**
     * Processing data
     * 
     * @param json
     * @return true: close connection; false: not
     */
    protected abstract boolean process(String json) throws Exception;

    protected void write(String msg) {
        if (outwriter != null) {
            outwriter.println(msg);
            outwriter.flush();
        }
    }

    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (outwriter != null) {
                outwriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
        catch (IOException e) {
            log.error("received exception closing the connection "
                    + getSocketAddr() + ": " + e);
        }
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    public String getSocketAddr() {
        if (socket == null) {
            return null;
        }
        return UtilHelper.getSocketAddr(socket);
    }

    public Command getCommand() {
        return com;
    }
}
