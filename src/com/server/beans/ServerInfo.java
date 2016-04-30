package com.server.beans;

public class ServerInfo implements ServerValue {

    private String id = null;
    private int port = 0;
    private String hostname = null;
    private String secret = null;
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
        return getSecret();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int localPort) {
        this.port = localPort;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String localHostname) {
        this.hostname = localHostname;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format(OUTPUT_FORMAT, getHostname(), getPort(),
                getSecret());
    }
}
