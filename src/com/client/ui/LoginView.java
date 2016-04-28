package com.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class LoginView extends JFrame implements ActionListener {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Demo application");
        frame.setSize(440, 330);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        JLabel label = new JLabel("Connect to server first:Menu->Connect");
        label.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(label);

        frame.add(bar, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    private static void placeComponents(JPanel panel) {
        ClientActionController control = new ClientActionController();

        panel.setLayout(null);

        JMenuBar menuBar;
        JMenuItem about;
        menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 440, 25);
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(70, 110, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(140, 110, 160, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(70, 170, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(140, 170, 160, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
        control.ListenLoginButton(loginButton);
        loginButton.setBounds(101, 230, 80, 25);

        panel.add(loginButton);

        JButton registerButton = new JButton("register");
        control.ListenregisterButton(registerButton);
        registerButton.setBounds(218, 230, 80, 25);
        panel.add(registerButton);

        panel.add(menuBar);

        JMenu menu;
        JMenu help;
        JMenuItem Connect;
        JMenuItem Disconnect;
        menu = new JMenu("Menu");

        help = new JMenu("help");
        menuBar.add(menu);
        menuBar.add(help);
        about = new JMenuItem("about");
        control.about(about);
        help.add(about);
        Connect = new JMenuItem("Connect");
        control.listenConnect(Connect);
        Disconnect = new JMenuItem("Disconnect");
        control.listenDisConnect(Disconnect);

        menu.add(Connect);

        menu.addSeparator();
        menu.add(Disconnect);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

}