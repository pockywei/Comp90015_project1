package com.protocal.connection.inter;

import com.protocal.conn.inter.ConnectionType;

public interface ConnectionListener {
    
    public void close() throws Exception;

    public void setConnectionType(ConnectionType type);
    
    /**
     * Send message
     * 
     * @param com
     * @param activity
     * @return true: send success; false: send failed as connection has been
     *         closed.
     */
    public boolean sendMessage(String msg);
    
}
