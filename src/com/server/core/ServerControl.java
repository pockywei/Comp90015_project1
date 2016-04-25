package com.server.core;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.base.BaseManager;
import com.server.ServerSettings;
import com.utils.UtilHelper;
import com.utils.log.CrashHandler;
import com.utils.protocal.Command;
import com.utils.protocal.connection.Connection;
import com.utils.protocal.connection.Request;
import com.utils.protocal.connection.Response;
import com.utils.protocal.connection.inter.SocketListener;

public abstract class ServerControl extends BaseManager
        implements SocketListener {

    /* Outgoing connection */
    private List<Request> requests = null;
    /* Incoming connection */
    private List<Response> responses = null;
    private ServerListener listener = null;

    public ServerControl() {
        super();
        requests = new ArrayList<>();
        responses = new ArrayList<>();
        try {
            listener = new ServerListener(this);
        }
        catch (IOException e) {
            log.fatal("failed to startup a listening thread: " + e);
            CrashHandler.getInstance().exit();
        }
        initServer();
        start();
    }

    public abstract void serverAnnounce();

    public abstract void initServer();

    @Override
    public void runTask() throws Exception {
        log.info("using activity interval of "
                + ServerSettings.getActivityInterval() + " milliseconds");
        while (!stop) {
            // do something with 5 second intervals in between
            try {
                Thread.sleep(ServerSettings.getActivityInterval());
            }
            catch (InterruptedException e) {
                log.info("received an interrupt, system is shutting down");
                break;
            }
            if (!stop) {
                log.info("doing Announce");
                serverAnnounce();
            }
        }
        log.info("closing " + responses.size() + " response connections");
        clear();
    }

    @Override
    public Response response(Socket s) throws IOException {
        log.debug("incomming connection: " + UtilHelper.getSocketAddr(s));
        Response response = null;
        synchronized (responses) {
            response = new Response(s);
            responses.add(response);
        }
        return response;
    }

    @Override
    public Request request(Socket s, Command com) throws IOException {
        log.debug("outgoing connection: " + UtilHelper.getSocketAddr(s));
        Request c = null;
        synchronized (requests) {
            c = new Request(s, com);
            requests.add(c);
        }
        return c;
    }

    public void removeConnection(Connection c) {
        if (c instanceof Request) {
            synchronized (requests) {
                requests.remove(c);
            }
        }
        else {
            synchronized (responses) {
                responses.remove(c);
            }
        }
    }

    @Override
    public void clear() {
        stop();
        if (listener != null) {
            listener.stop();
        }
        DataTable.getInstance().clear();
        synchronized (requests) {
            log.info("closing " + requests.size() + " request connections");
            for (Connection c : requests) {
                c.close();
            }
            requests.clear();
        }
        synchronized (responses) {
            log.info("closing " + responses.size() + " response connections");
            for (Connection c : responses) {
                c.close();
            }
            responses.clear();
        }
    }
}
