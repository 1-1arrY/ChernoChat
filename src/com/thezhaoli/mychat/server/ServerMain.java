package com.thezhaoli.mychat.server;

import com.thezhaoli.mychat.server.Server;

public class ServerMain {
    private int port;
    private Server server;

    public ServerMain(int port) {
        this.port = port;
        server = new Server(port);
    }

    public static void main(String[] args) {
        int port;
        if (args.length != 1) {
            System.out.println("Usage: java -jar MyChatServer.jar [port]");
            return;
        }
        port = Integer.parseInt(args[0]);
        new ServerMain(port);
    }
}
