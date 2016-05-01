package com.protocal.connection.inter;

import com.protocal.connection.Connection;

public interface ConnectionListener {

    public void closeConnection(Connection c) throws Exception;

    public void addConnection(Connection c);

}
