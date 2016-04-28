package com.protocal.conn.inter;

public interface Response {

    /**
     * Process received message
     * 
     * @param json
     * @param connection
     * @return true: close connection; false: not
     * @throws Exception
     */
    public boolean process(String json, ConnectionListener connection)
            throws Exception;
}
