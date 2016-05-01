package com.client.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

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

import com.base.BaseFrame;
import com.protocal.Command;

public class LoginFrame extends BaseFrame {

    private static final long serialVersionUID = 1L;

    @Override
    public void initView() {
        setSize(440, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        JLabel label = new JLabel("Connect to server first:Menu->Connect");

        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(label);

        add(bar, BorderLayout.SOUTH);
    }

    private void placeComponents(JPanel panel) {
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
        control.listenLoginButton(loginButton, userText, passwordText);
        loginButton.setBounds(50, 230, 80, 25);

        panel.add(loginButton);

        JButton registerButton = new JButton("register");
        control.listenRegisterButton(registerButton, this);
        registerButton.setBounds(160, 230, 80, 25);
        panel.add(registerButton);

        JButton anonymousLogin = new JButton("Login as guest");
        control.listenGuestLoginButton(anonymousLogin, this);
        anonymousLogin.setBounds(270, 230, 130, 25);
        panel.add(anonymousLogin);

        panel.add(menuBar);

        JMenu menu;
        JMenu help;
        JMenuItem Connect;
        menu = new JMenu("Menu");

        help = new JMenu("help");
        menuBar.add(menu);
        menuBar.add(help);
        about = new JMenuItem("about");
        control.about(about);
        help.add(about);
        Connect = new JMenuItem("Connect");
        control.listenConnect(Connect);
        menu.add(Connect);
    }

    @Override
    public void actionSuccess(Command com, String info) {
        switch (com) {
            case REGISTER_SUCCESS:
                JOptionPane.showMessageDialog(null, info);
                break;
            case LOGIN_SUCCESS:
                nextFrame(MessageFrame.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionFailed(Command com, String info) {
        JOptionPane.showMessageDialog(null, info);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // do nothing...
    }
}