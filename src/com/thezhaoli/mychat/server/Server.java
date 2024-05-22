package com.thezhaoli.mychat.server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private List<UserThread> clients= new ArrayList<>();
    private int port;
    private ServerSocket server;
    private boolean running = false;
    private Thread run, receive;
    public Server(int port) {
        this.port = port;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        run = new Thread(this, "Server");
        run.start();
    }

    public void run() {
        running = true;
        System.out.println("服务器开始监听端口:" + port);
        receive();
    }

    private void receive() {
        receive = new Thread("Receive"){
            //线程池
            ExecutorService es = Executors.newFixedThreadPool(5);
            public void run() {
                while (running) {
                    Socket clientSocket = null;
                    try {
                        clientSocket = server.accept();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    UserThread user = new UserThread(clientSocket,clients);
                    es.execute(user);  //开启线程
                }
            }
        };
        receive.start();
    }

}
