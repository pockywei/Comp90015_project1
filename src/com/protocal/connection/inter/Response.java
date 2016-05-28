package com.protocal.connection.inter;

import com.protocal.connection.Connection;

public interface Response {

    /**
     * Process received message
     * 
     * @param json
     * @param connection
     * @return true: close connection; false: not
     * @throws Exception
     */
    public boolean preProcess(String json, Connection connection)
            throws Exception;
}
