/*
 * Created by JFormDesigner on Wed May 01 09:40:08 CST 2024
 */

package com.thezhaoli.mychat;


import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
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
            System.err.println("连接失败!");
            console("连接失败!");
        }
        initComponents();
        setVisible(true);
        txtMessage.requestFocusInWindow();
        console("正在尝试连接 " + address + ":" + port + ", User:" + name);
        String connection = "/c/" + name;
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
            if (txtMessage.getText().isEmpty()) return;
            message = client.getName() + ":" + message;
            message = "/m/" + message;
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
                    if (message.startsWith("/m/")) {
                        String text = message.substring(3);
                        //text = text.split("/e/")[0];
                        console(text);
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
        String disconnect = "/d/";
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
