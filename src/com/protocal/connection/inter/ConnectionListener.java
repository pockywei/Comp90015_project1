package com.protocal.connection.inter;

import com.protocal.conn.ConnectionType;

public interface ConnectionListener {
    public void close() throws Exception;

    public void setConnectionType(ConnectionType type);
}
