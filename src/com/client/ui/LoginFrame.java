package com.client.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
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
import javax.swing.border.EmptyBorder;

import com.base.BaseFrame;
import com.protocal.Command;

public class LoginFrame extends BaseFrame {

    private static final long serialVersionUID = 1L;
    private static final int BUTTON_HEIGHT = 25;
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 250;
    private static final int INDENT_WIDTH = 50;
    private static final int TEXT_WIDTH = 90;
    private static final int TEXT_EXTRA_WIDTH = 50;
    private static final int INPUT_SIZE = 20;
    private static final int TOP_MARGIN = 20;

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
                "Please add remote server info first : [Menu -> Connect]"));
        add(bar, BorderLayout.SOUTH);
    }

    /**
     * Add empty area for top margin
     * 
     * @param margin
     * @return
     */
    private JPanel addTopMargin(int margin) {
        JPanel emptyArea = new JPanel();
        emptyArea.setSize(FRAME_WIDTH, margin);
        return emptyArea;
    }

    private void placeComponents(JPanel panel, ActionController control) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // add areas
        panel.add(addTopMargin(TOP_MARGIN));
        JPanel userNameArea = new JPanel();
        userNameArea.setSize(FRAME_WIDTH, BUTTON_HEIGHT);
        userNameArea.setLayout(null);
        panel.add(userNameArea);

        panel.add(addTopMargin(TOP_MARGIN));
        JPanel passwordArea = new JPanel();
        passwordArea.setSize(FRAME_WIDTH, BUTTON_HEIGHT);
        passwordArea.setLayout(null);
        panel.add(passwordArea);

        JPanel actionArea = new JPanel();
        actionArea.setSize(FRAME_WIDTH, BUTTON_HEIGHT);
        actionArea.setBorder(new EmptyBorder(TOP_MARGIN, 0, 0, 0));
        panel.add(actionArea);

        // add input areas
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(INDENT_WIDTH, 0, TEXT_WIDTH, BUTTON_HEIGHT);
        userNameArea.add(userLabel);

        JTextField userText = new JTextField(INPUT_SIZE);
        userText.setBounds(INDENT_WIDTH + TEXT_WIDTH, 0, 2 * TEXT_WIDTH,
                BUTTON_HEIGHT);
        userNameArea.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(INDENT_WIDTH, 0, TEXT_WIDTH, BUTTON_HEIGHT);
        passwordArea.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(INPUT_SIZE);
        passwordText.setBounds(INDENT_WIDTH + TEXT_WIDTH, 0, 2 * TEXT_WIDTH,
                BUTTON_HEIGHT);
        passwordArea.add(passwordText);

        // add three actions buttons
        JButton loginButton = new JButton("Login");
        control.listenLoginButton(loginButton, userText, passwordText);
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