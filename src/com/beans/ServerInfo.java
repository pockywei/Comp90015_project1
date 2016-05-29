package com.beans;

import com.protocal.Protocal;
import com.utils.UtilHelper;

public class ServerInfo implements Record, Comparable<ServerInfo> {

    private String id = "";
    private int port = 0;
    private String hostname = "";
    private String secret = "";
    private static final String OUTPUT_FORMAT = "IP:<%s:%d> secret:%s";
    private int load = 0;

    public ServerInfo() {
    }

    public ServerInfo(String id, int port, String hostname, String secret) {
        this.id = id;
        this.port = port;
        this.hostname = hostname;
        this.secret = secret;
    }

    public ServerInfo(String id, int port, String hostname, String secret,
            int load) {
        this(id, port, hostname, secret);
        this.load = load;
    }

    @Override
    public String getKey() {
        return String.format(Protocal.SOCKET_ADDRESS, getHostname(), getPort());
    }

    public int getPort() {
        return port;
    }

    public ServerInfo setPort(int localPort) {
        this.port = localPort;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public ServerInfo setHostname(String localHostname) {
        this.hostname = localHostname;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public ServerInfo setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        // the server id will be updated once only.
        if (UtilHelper.isEmptyStr(this.id)) {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return String.format(OUTPUT_FORMAT, getHostname(), getPort(),
                getSecret());
    }

    @Override
    public int compareTo(ServerInfo another) {
        // descending order.
        return getLoad() - another.getLoad();
    }

    @Override
    public boolean equals(Object o) {
        return getKey().equals(((Record) o).getKey());
    }
}
