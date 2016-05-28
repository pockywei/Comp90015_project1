package com.server.core.response;

import com.protocal.Message;
import com.protocal.connection.Connection;
import com.protocal.connection.inter.ConnectionType;
import com.server.core.ServerManager;
import com.utils.log.CrashHandler;

public class ServerResponse extends AbstractResponse {

    @Override
    public boolean process(Message msg, Connection connection)
            throws Exception {
        switch (msg.getCommand()) {
            /*
             * response to the server side.
             */
            case LOCK_REQUEST:
                return new LockRequestResponse(msg.toUserInfo()).process(msg,
                        connection);
            case SERVER_ANNOUNCE:
                // update local storge with the server info
                ServerManager.getInstance().updateServerInfo(msg.toServerInfo(),
                        connection);
                // broadcast to other adjacent servers
                ServerManager.getInstance().sendAnnounce(connection);
                return false;
            case ACTIVITY_BROADCAST:
                ServerManager.getInstance().sendActivityBroadcast(connection,
                        msg.getActivity().getUsername(),
                        msg.getActivity().getMessage());
                return false;
            case LOCK_ALLOWED:
                return new LockResponse(msg.toUserInfo(), true).process(msg,
                        connection);
            case LOCK_DENIED:
                return new LockResponse(msg.toUserInfo(), false).process(msg,
                        connection);
            case AUTHENTICATION_FAIL:
            case INVALID_MESSAGE:
                log.error(msg.getInfo());
                if (connection != null
                        && connection.getType() == ConnectionType.SERVER_CONN) {
                    // authenticate failed
                    CrashHandler.getInstance().errorExit();
                }
                return true;
            /*
             * response to the client side.
             */
            case LOGOUT:
                // close connection.
                return true;
            case ACTIVITY_MESSAGE:
                // send a message broadcast to all adjacent servers and
                // clients.
                ServerManager.getInstance().sendActivityBroadcast(connection,
                        msg.toUserInfo().getUsername(),
                        msg.getActivity().getMessage());
                return false;
            default:
                break;
        }
        // no cases matched, close connection.
        return true;
    }
}
