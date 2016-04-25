package com.server.beans;

public class ServerInfo implements ServerValue {

    private int port = 3780;
    private String hostname = "localhost";
    private String secret = null;
    private static final String OUTPUT_FORMAT = "IP:<%s:%d> secret:%s";
    private int load = 0;

    public ServerInfo() {
    }

    public ServerInfo(int port, String hostname, String secret) {
        this.port = port;
        this.hostname = hostname;
        this.secret = secret;
    }

    public ServerInfo(int port, String hostname, String secret, int load) {
        this(port, hostname, secret);
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

    @Override
    public String toString() {
        return String.format(OUTPUT_FORMAT, getHostname(), getPort(),
                getSecret());
    }
}
