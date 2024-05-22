package com.thezhaoli.mychat;

import java.io.IOException;
import java.net.*;

public class Client {
    private String name, address;
    private int port;
    private InetAddress ip;
    private DatagramSocket socket;
    private Thread send;
    private int ID = -1;

    public Client(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
            socket = new DatagramSocket();
            ip = InetAddress.getByName(address);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public String receive() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e)  {
            e.printStackTrace();
        }
        String message = new String(packet.getData());
        return message;
    }

    public void send(byte[] data) {
        send = new Thread("send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data,data.length,ip,port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    public void close() {
        new Thread(() -> {
            synchronized (socket) {
                socket.close();
            }
        }).start();

    }
}
