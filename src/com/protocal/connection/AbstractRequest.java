package com.protocal.connection;

import com.protocal.Message;
import com.protocal.json.JsonBuilder;
import com.utils.UtilHelper;
import com.utils.log.Log;

public abstract class AbstractRequest {

    protected static final Log log = Log.getInstance();
    private Connection connection = null;

    public AbstractRequest(Connection connection) {
        this.connection = connection;
    }

    public abstract Message getRequestMessage();

    /**
     * true: send success; false: send failed as connection has been closed.
     */
    public boolean request() {
        if (connection == null) {
            return false;
        }
        String json = new JsonBuilder(getRequestMessage()).getJson();
        if (UtilHelper.isEmptyStr(json)) {
            return false;
        }
        return connection.sendMessage(json);
    }
}
