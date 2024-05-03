package com.thezhaoli.chernochat.server;

import com.thezhaoli.chernochat.Client;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private List<ServerClient> clients= new ArrayList<>();
    private List<Integer> clientRespond = new ArrayList<>();
    private int port;
    private DatagramSocket socket;
    private boolean running = false;
    private Thread run, manage, receive, send;
    private final int MAX_ATTEMPT = 5;
    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        run = new Thread(this, "Server");
        run.start();
    }

    public void run() {
        running = true;
        System.out.println("Server started on " + port);
        manageClients();
        receive();
    }

    private void manageClients() {
        manage = new Thread("Manage"){
            public void run() {
                while (running) {
                    sendToAll("/i/server");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < clients.size(); i++) {
                        ServerClient c = clients.get(i);
                        if(!clientRespond.contains(c.getID())) {
                            if (c.attempt >= MAX_ATTEMPT) {
                                disconnect(c.getID(), false);
                            } else {
                                c.attempt++;
                            }
                        } else {
                            clientRespond.remove(Integer.valueOf(c.getID()));
                            c.attempt = 0;
                        }
                    }
                }
            }
        };
        manage.start();
    }

    private void sendToAll(String message) {
        for (ServerClient client : clients) {
            send(message.getBytes(), client.address, client.port);
        }
    }

    private void send(byte[] data, InetAddress address, int port) {
        send = new Thread("send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    private void send(String message, InetAddress address, int port) {
        message += "/e/";
        send(message.getBytes(), address, port);
    }

    private void receive() {
        receive = new Thread("Receive"){
            public void run() {
                while (running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data,data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
                }
            }
        };
        receive.start();
    }
    private void process(DatagramPacket packet) {
        String string = new String(packet.getData());
        if (string.startsWith("/c/")) {
            String name = string.split("/c/|/e/")[1];
            int id = UniqueIdentifier.getIdentifier();
            clients.add(new ServerClient(name, packet.getAddress(), packet.getPort(), id));
            System.out.println(name);
            System.out.println(packet.getAddress().toString() + ":" + packet.getPort());
            String ID = "/c/" + id;
            send(ID, packet.getAddress(), packet.getPort());
        } else if (string.startsWith("/m/")){
            sendToAll(string);
        } else if (string.startsWith("/d/")) {
            int id = Integer.parseInt(string.split("/d/|/e/")[1]);
            disconnect(id, true);
        }else if (string.startsWith("/i/")){
            int id = Integer.parseInt(string.split("/i/|/e/")[1]);
            clientRespond.add(id);
        } else {
            System.out.println(string);
        }
    }

    private void disconnect(int id, boolean status) {
        ServerClient c = null;
        for (ServerClient client : clients) {
            if (client.getID() == id) {
                c = client;
                clients.remove(client);
                break;
            }
        }

        String message;
        if (status == true) {
            message = c.name + "(" + c.getID() + ") @ " + c.address.toString() + ":" + c.port + " Disconnected";
        } else {
            message = c.name + "(" + c.getID() + ") @ " + c.address.toString() + ":" + c.port + " Time out";
        }
        System.out.println(message);
    }
}
