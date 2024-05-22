package com.thezhaoli.mychat.server;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

class UserThread implements Runnable{
    private String name; //客户端的用户名称
    private Socket socket;
    private List<UserThread> list;   //客户端处理线程的集合
    public InputStream in;    //输入流
    public OutputStream out;  //输出流
    private boolean flag = true;  //标记

    public UserThread(Socket socket, List<UserThread> list) {
        this.socket = socket;
        this.list = list;
        list.add(this);    //把当前线程也加入List中
    }

    @Override
    public void run() {
        try {
            //1、构造输入输出流
            System.out.println("用户：" + socket.getInetAddress().getHostAddress() + "已连接！");
            in = socket.getInputStream();
            out = socket.getOutputStream();
            //2、循环读取
            while(flag){
                byte[] bytes = new byte[1024];
                int i = 0;
                try {
                    i = in.read(bytes);
                } catch (SocketException e) {
                    disconnect(false);//以异常状态关闭连接
                    break;
                }

                String message = new String(bytes, 0, i);
                process(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void process(String string) throws IOException {
        if (string.startsWith("/c/") && name == null) {
            name = string.substring(3);
            String message = "/m/用户：" + name + " 加入群聊";
            sendToAll(message);
        }else if (string.startsWith("/d/")){
            disconnect(true); //以正常状态关闭连接
        } else {
            sendToAll(string);
        }
    }

    private void sendToAll(String message) throws IOException {
        for (UserThread ut : list) {
            ut.out.write(message.getBytes());
        }
    }

    private void disconnect(boolean status) {
        list.remove(this);
        flag = false;
        String message;
        if (status) {
            message = name + " @ " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " 断开连接";
        } else {
            message = name + " @ " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " 超时";
        }
        System.out.println(message);
    }


}


