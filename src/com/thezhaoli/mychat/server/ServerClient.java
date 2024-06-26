package com.thezhaoli.mychat.server;

import java.net.InetAddress;

public class ServerClient {
    public String name;
    public InetAddress address;
    public int port;
    private final int ID;
    public int attempt = 0;

    ServerClient(String name, InetAddress address, int port, int ID) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
