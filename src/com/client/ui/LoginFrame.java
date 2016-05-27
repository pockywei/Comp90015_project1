package com.client.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import com.base.BaseFrame;
import com.protocal.Command;

public class LoginFrame extends BaseFrame {

    private static final long serialVersionUID = 1L;
    private static final int BUTTON_HEIGHT = 30;
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 250;
    private static final int INDENT_WIDTH = 50;
    private static final int TEXT_WIDTH = 90;
    private static final int TEXT_EXTRA_WIDTH = 50;
    private static final int MARGIN = 10;

    private JTextField userText;
    private JPasswordField passwordText;

    @Override
    public void initView() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ActionController control = new ActionController();
        setLayout(new BorderLayout());

        // add menu bar
        JMenuBar menuBar;
        menuBar = new JMenuBar();
        menuBar.setSize(FRAME_WIDTH, BUTTON_HEIGHT);
        initMenu(menuBar, control);
        add(menuBar, BorderLayout.NORTH);

        // main area
        JPanel panel = new JPanel();
        placeComponents(panel, control);
        add(panel, BorderLayout.CENTER);

        // footer area
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(new JLabel(
                "Please add remote server info first : [Menu -> RemoteSever]"));
        add(bar, BorderLayout.SOUTH);
    }

    private void placeComponents(JPanel panel, ActionController control) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // add input area
        panel.add(addTextField());
        // add three buttons
        panel.add(addButtons(control));
    }

    private JPanel addTextField() {
        JPanel inputArea = new JPanel();
        inputArea.setLayout(new GridBagLayout());
        inputArea.setBorder(
                new EmptyBorder(0, INDENT_WIDTH, 0, 2 * INDENT_WIDTH));

        JLabel userLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        userText = new JTextField(UIHelper.INPUT_SIZE);
        passwordText = new JPasswordField(UIHelper.INPUT_SIZE);

        GridBagConstraints cell = UIHelper.makeGbc(0, 0, 0);
        inputArea.add(userLabel, cell);

        cell = UIHelper.makeGbc(1, 0, MARGIN, 1);
        inputArea.add(userText, cell);

        cell = UIHelper.makeGbc(0, 1, 0);
        inputArea.add(passwordLabel, cell);

        cell = UIHelper.makeGbc(1, 1, MARGIN, 1);
        inputArea.add(passwordText, cell);
        return inputArea;
    }

    private JPanel addButtons(ActionController control) {
        JPanel actionArea = new JPanel();
        actionArea.setSize(FRAME_WIDTH, BUTTON_HEIGHT);
        actionArea.setBorder(new EmptyBorder(MARGIN, 0, 0, 0));

        // add three actions buttons
        JButton loginButton = new JButton("Login");
        control.listenLoginButton(loginButton, userText, passwordText, this);
        loginButton.setSize(TEXT_WIDTH, BUTTON_HEIGHT);
        actionArea.add(loginButton);

        JButton registerButton = new JButton("SignUp");
        control.listenRegisterButton(registerButton, this);
        registerButton.setSize(TEXT_WIDTH, BUTTON_HEIGHT);
        actionArea.add(registerButton);

        JButton anonymousLogin = new JButton("Login as guest");
        control.listenGuestLoginButton(anonymousLogin, this);
        anonymousLogin.setSize(TEXT_WIDTH + TEXT_EXTRA_WIDTH, BUTTON_HEIGHT);
        actionArea.add(anonymousLogin);
        return actionArea;
    }

    private void initMenu(JMenuBar menuBar, ActionController control) {
        // add first menu button
        JMenu menu;
        JMenu help;
        menu = new JMenu("Menu");
        help = new JMenu("Help");
        menuBar.add(menu);
        menuBar.add(help);

        // add second menu button
        JMenuItem about = new JMenuItem("About");
        control.about(about);
        help.add(about);
        JMenuItem Connect = new JMenuItem("RemoteServer");
        control.listenConnect(Connect, this);
        menu.add(Connect);
    }

    @Override
    public void actionSuccess(Command com, String info, Object o) {
        switch (com) {
            case REGISTER_SUCCESS:
                UIHelper.showMessageDialog(info);
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
        UIHelper.showMessageDialog(info);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // do nothing...
    }
}