package com.client.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.base.BaseFrame;
import com.client.UserSettings;
import com.client.core.ClientManger;
import com.protocal.Protocal;
import com.utils.UtilHelper;
import com.utils.log.Log;

public class ClientActionController {

    private static final Log log = Log.getInstance();

    /**
     * Setting up the Remote server info.
     * 
     * @param con
     */
    public void listenConnect(JMenuItem con) {

        con.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JTextField xField = new JTextField(10);
                JTextField yField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new GridLayout(2, 0));
                JLabel hostname = new JLabel("Hostname");

                myPanel.add(hostname);
                myPanel.add(xField);

                JLabel portnumber = new JLabel("Port No.");
                myPanel.add(portnumber);
                myPanel.add(yField);

                // add previous server info.
                xField.setText(UserSettings.getRemoteHost());
                yField.setText(UserSettings.getRemotePort() + "");

                int input = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter Remote Address and Remote Port",
                        JOptionPane.OK_CANCEL_OPTION);
                if (input == JOptionPane.OK_OPTION) {
                    String host = xField.getText().trim();
                    int port = 0;
                    try {
                        port = Integer.parseInt(yField.getText().trim());
                    }
                    catch (NumberFormatException e) {
                        log.error("the number of port is invalid.");
                    }
                    UserSettings.setServerInfo(port, host);
                }
            }
        });

    }

    public void about(JMenuItem abt) {

        abt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                        "<html><center>COMP90015 PROJECT1 VER 1.0<br>by the Eyebrow</center></html>");
            }
        });

    }

    public void listenLoginButton(JButton login_button,
            final JTextField username, final JPasswordField password) {

        login_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String user = username.getText().trim();
                String secret = new String(password.getPassword());
                if (!UtilHelper.isEmptyStr(user)
                        && !UtilHelper.isEmptyStr(secret)) {
                    try {
                        UserSettings.setUser(user, secret);
                        ClientManger.getInstance().sendLoginRequest();
                        return;
                    }
                    catch (Exception e) {
                        log.error("login request send failed by the exception "
                                + e);
                        JOptionPane.showMessageDialog(null,
                                "login request failed, please check the remote info.");
                    }
                    return;
                }
                JOptionPane.showMessageDialog(null,
                        "username or password can not be empty, please try again.");
            }
        });

    }

    public void listenGuestLoginButton(JButton GuestLoginButton,
            final BaseFrame frame) {

        GuestLoginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (UtilHelper.isEmptyStr(UserSettings.getRemoteHost())
                        || UserSettings.getRemotePort() == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Please set up the remote info first.");
                    return;
                }

                // the current user is anonymous, turn to the message frame.
                UserSettings.setUser(Protocal.ANONYMOUS, "");
                frame.nextFrame(MessageFrame.class);
            }
        });
    }

    public void listenRegisterButton(JButton register_Button,
            final BaseFrame frame) {

        register_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                new RegisterDialog(frame);
            }
        });

    }
}
