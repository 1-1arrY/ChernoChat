/*
 * Created by JFormDesigner on Tue Apr 30 20:20:02 CST 2024
 */

package com.thezhaoli.mychat;

import com.thezhaoli.mychat.ClientWindow;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * @author 26041
 */
public class Login extends JFrame {
    public Login() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
    }

    private void LoginButton(ActionEvent e) {
        // TODO add your code here
        String name = txtName.getText();
        String address = txtAddress.getText();
        int port = Integer.parseInt(txtPort.getText());
        login(name, address, port);
    }

    private void login(String name, String address, int port) {
        dispose();
        new ClientWindow(name, address, port);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("com.thezhaoli.mychat.login");
        label1 = new JLabel();
        txtName = new JTextField();
        txtAddress = new JTextField();
        label2 = new JLabel();
        txtPort = new JTextField();
        label3 = new JLabel();
        btnLogin = new JButton();
        lblPort = new JLabel();
        lblAddress = new JLabel();

        //======== this ========
        setTitle("Login");
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("Name:");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 3f));
        contentPane.add(label1);
        label1.setBounds(140, 40, 55, 42);

        //---- txtName ----
        txtName.setFont(txtName.getFont().deriveFont(txtName.getFont().getSize() + 3f));
        contentPane.add(txtName);
        txtName.setBounds(65, 75, 210, txtName.getPreferredSize().height);

        //---- txtAddress ----
        txtAddress.setFont(txtAddress.getFont().deriveFont(txtAddress.getFont().getSize() + 3f));
        contentPane.add(txtAddress);
        txtAddress.setBounds(65, 150, 210, txtAddress.getPreferredSize().height);

        //---- label2 ----
        label2.setText("IP Address:");
        label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 3f));
        contentPane.add(label2);
        label2.setBounds(115, 120, 115, 42);

        //---- txtPort ----
        txtPort.setFont(txtPort.getFont().deriveFont(txtPort.getFont().getSize() + 3f));
        contentPane.add(txtPort);
        txtPort.setBounds(65, 245, 210, txtPort.getPreferredSize().height);

        //---- label3 ----
        label3.setText("Port:");
        label3.setFont(label3.getFont().deriveFont(label3.getFont().getSize() + 3f));
        contentPane.add(label3);
        label3.setBounds(145, 215, 65, 42);

        //---- btnLogin ----
        btnLogin.setText(bundle.getString("Login.btnLogin.text"));
        btnLogin.addActionListener(e -> LoginButton(e));
        contentPane.add(btnLogin);
        btnLogin.setBounds(105, 330, 123, btnLogin.getPreferredSize().height);

        //---- lblPort ----
        lblPort.setText(bundle.getString("Login.lblPort.text"));
        contentPane.add(lblPort);
        lblPort.setBounds(new Rectangle(new Point(130, 280), lblPort.getPreferredSize()));

        //---- lblAddress ----
        lblAddress.setText(bundle.getString("Login.lblAddress.text"));
        contentPane.add(lblAddress);
        lblAddress.setBounds(new Rectangle(new Point(105, 185), lblAddress.getPreferredSize()));

        contentPane.setPreferredSize(new Dimension(286, 370));
        setSize(357, 462);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    private JTextField txtName;
    private JTextField txtAddress;
    private JLabel label2;
    private JTextField txtPort;
    private JLabel label3;
    private JButton btnLogin;
    private JLabel lblPort;
    private JLabel lblAddress;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login frame = new Login();
            frame.setVisible(true);
        });
    }
}
