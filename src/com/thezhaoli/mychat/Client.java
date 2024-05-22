package com.thezhaoli.mychat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Client {
    private String name, address;
    private int port;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Thread send;

    public Client(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public boolean openConnection(String address) {
        try {
            socket = new Socket(address, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public String receive() {
        byte[] bytes = new byte[1024];
        int i = 0;
        try {
            i = in.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String(bytes, 0, i);
    }

    public void send(byte[] data) {
        send = new Thread("send") {
            public void run() {
                try {
                    out.write(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        send.start();
    }

    public void close() {
        new Thread(() -> {
            synchronized (socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
}
