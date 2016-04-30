package com.protocal.connection.inter;

import com.protocal.connection.Connection;

public interface ConnectionListener {

    public void close() throws Exception;

    public void addConnection(Connection c);

}
