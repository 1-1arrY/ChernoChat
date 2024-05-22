/*
 * Created by JFormDesigner on Wed May 01 09:40:08 CST 2024
 */

package com.thezhaoli.mychat;

import com.thezhaoli.mychat.Client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serial;
import java.net.*;
import java.sql.SQLOutput;
import java.util.*;
import javax.swing.*;

public class ClientWindow extends JFrame implements Runnable{
    @Serial
    private static final long serialVersionUID = 1L;
    private Client client;
    private Thread run, listen;
    private boolean running = false;

    public ClientWindow(String name, String address, int port) {
        client = new Client(name, address, port);
        boolean connect = client.openConnection(address);
        if (!connect) {
            System.err.println("Connection failed!");
            console("Connection failed!");
        }
        initComponents();
        setVisible(true);
        txtMessage.requestFocusInWindow();
        console("Attempting to connect " + address + ":" + port + ", User:" + name);
        String connection = "/c/" + name + "/e/";
        client.send(connection.getBytes());
        run = new Thread(this, "Running");
        run.start();
    }

    public void run() {
        running = true;
        listen();
    }

    private void send(String message, boolean text) {
        if (text){
            if (txtMessage.getText().equals("")) return;
            message = client.getName() + ":" + message;
            message = "/m/" + message + "/e/";
            txtMessage.setText("");
        }
        client.send(message.getBytes());
    }

    private void console(String message) {
        history.setCaretPosition(history.getDocument().getLength());
        history.append(message + "\n\r");
    }

    private void listen() {
        listen = new Thread("Listen") {
            public void run() {
                while (running) {
                    String message = client.receive();
                    if (message.startsWith("/c/")) {
                        client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
                        console("Successful connected to server!  ID:" + client.getID());
                    } else if (message.startsWith("/m/")) {
                        String text = message.substring(3);
                        text = text.split("/e/")[0];
                        console(text);
                    } else if (message.startsWith("/i/")) {
                        String text = "/i/" + client.getID() + "/e/";
                        send(text,false);
                    }
                }
            }
        };
        listen.start();
    }

    private void btnSend(ActionEvent e) {
        // TODO add your code here
        send(txtMessage.getText(), true);
    }

    private void txtMessageKeyPressed(KeyEvent e) {
        // TODO add your code here
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            send(txtMessage.getText(), true);
        }
    }

    private void thisWindowClosing(WindowEvent e) {
        String disconnect = "/d/" + client.getID() + "/e/";
        send(disconnect, false);
        running = false;
        client.close();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("com.thezhaoli.mychat.login");
        scrollPane2 = new JScrollPane();
        history = new JTextArea();
        txtMessage = new JTextField();
        btnSend = new JButton();

        //======== this ========
        setTitle("cherno chat client");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {25, 831, 154, 16, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {442, 49, 13, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0, 0.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};

        //======== scrollPane2 ========
        {

            //---- history ----
            history.setEditable(false);
            history.setFont(history.getFont().deriveFont(history.getFont().getSize() + 3f));
            scrollPane2.setViewportView(history);
        }
        contentPane.add(scrollPane2, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(6, 0, 9, 9), 0, 0));

        //---- txtMessage ----
        txtMessage.setFont(txtMessage.getFont().deriveFont(txtMessage.getFont().getSize() + 3f));
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                txtMessageKeyPressed(e);
            }
        });
        contentPane.add(txtMessage, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 9, 9), 0, 0));

        //---- btnSend ----
        btnSend.setText(bundle.getString("Client.btnSend.text"));
        btnSend.addActionListener(e -> btnSend(e));
        contentPane.add(btnSend, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 9, 9), 0, 0));
        setSize(987, 594);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JScrollPane scrollPane2;
    private JTextArea history;
    private JTextField txtMessage;
    private JButton btnSend;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
