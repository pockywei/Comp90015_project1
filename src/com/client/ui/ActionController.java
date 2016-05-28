package com.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.base.AsyncRunnable.Callback;
import com.base.BaseFrame;
import com.client.UserSettings;
import com.client.core.ClientManger;
import com.client.ui.ActionDialog.ActionDialogListener;
import com.protocal.Protocal;
import com.utils.UtilHelper;
import com.utils.log.Log;

public class ActionController {

    private static final Log log = Log.getInstance();

    /**
     * Setting up the Remote server info.
     * 
     * @param con
     */
    public void listenConnect(JMenuItem con, final BaseFrame frame) {

        con.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                new ActionDialog(frame, "Please Enter Remote Info",
                        UIHelper.getRemoteHeader(), new ActionDialogListener() {

                    @Override
                    public void confirmPerformed(ActionDialog dialog,
                            List<JTextField> fields) {
                        // get text fields' reference
                        String host = fields.get(0).getText().trim();
                        if (UtilHelper.isEmptyStr(host)) {
                            UIHelper.showMessageDialog(
                                    "the remote hostname can not be empty.");
                            return;
                        }
                        int port = 0;
                        try {
                            port = Integer
                                    .parseInt(fields.get(1).getText().trim());
                        }
                        catch (NumberFormatException e) {
                            UIHelper.showMessageDialog(
                                    "the number of port is invalid.");
                        }
                        UserSettings.setServerInfo(port, host);
                        dialog.close();
                    }
                });
            }
        });

    }

    public void listenLoginButton(JButton login_button,
            final JTextField username, final JPasswordField password,
            final BaseFrame frame) {

        login_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (!isRemoteReady())
                    return;
                String user = username.getText().trim();
                String secret = new String(password.getPassword());
                if (UtilHelper.isEmptyStr(user)
                        || UtilHelper.isEmptyStr(secret)) {
                    UIHelper.showMessageDialog(
                            "username or password can not be empty, please try again.");
                    return;
                }
                login(user, secret, frame);
            }
        });

    }

    public void listenGuestLoginButton(JButton GuestLoginButton,
            final BaseFrame frame) {

        GuestLoginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (!isRemoteReady())
                    return;
                login(Protocal.ANONYMOUS, "", frame);
            }
        });
    }

    public void listenRegisterButton(JButton register_Button,
            final BaseFrame frame) {

        register_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (!isRemoteReady())
                    return;
                new ActionDialog(frame, "Register", UIHelper.REGISTER_HEADER,
                        new ActionDialogListener() {

                    @Override
                    public void confirmPerformed(ActionDialog dialog,
                            List<JTextField> fields) {
                        // get text fields' reference
                        String user = fields.get(0).getText();
                        String secret = new String(
                                ((JPasswordField) fields.get(1)).getPassword());
                        String re_secret = new String(
                                ((JPasswordField) fields.get(2)).getPassword());

                        if (UtilHelper.isEmptyStr(user)
                                || UtilHelper.isEmptyStr(secret)
                                || UtilHelper.isEmptyStr(re_secret)) {
                            UIHelper.showMessageDialog(
                                    "username or password can not be empty, please try again.");
                            return;
                        }

                        if (!secret.equals(re_secret)) {
                            UIHelper.showMessageDialog(
                                    "the passwords are not equal, please try again.");
                            return;
                        }

                        register(user, secret, frame);
                        dialog.close();
                    }
                });
            }
        });

    }

    public void about(JMenuItem abt) {

        abt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                UIHelper.showMessageDialog(
                        "<html><center>COMP90015 PROJECT1 VER 1.0<br>by the Eyebrow</center></html>");
            }
        });

    }

    private boolean isRemoteReady() {
        if (UtilHelper.isEmptyStr(UserSettings.getRemoteHost())
                || UserSettings.getRemotePort() == 0) {
            UIHelper.showMessageDialog(
                    "Please set up the remote info first : [Menu -> RemoteSever]");
            return false;
        }
        return true;
    }

    private void register(String username, String secret,
            final BaseFrame frame) {
        try {
            frame.showProgress();
            ClientManger.getInstance().sendRegisterRequest(username, secret,
                    new Callback() {

                        @Override
                        public void getResult(boolean isSuccess) {
                            frame.closeProgress();
                        }
                    });
        }
        catch (Exception e1) {
            log.error("register request send failed by the exception " + e1);
            UIHelper.showMessageDialog(
                    "register request failed, please check the remote info.");
        }
    }

    private void login(String username, String secret, final BaseFrame frame) {
        try {
            // the current user is anonymous, turn to the message frame.
            UserSettings.setUser(username, secret);
            frame.showProgress();
            ClientManger.getInstance().sendLoginRequest(new Callback() {

                @Override
                public void getResult(boolean isSuccess) {
                    frame.closeProgress();
                }
            });
        }
        catch (Exception e) {
            log.error("login request send failed by the exception " + e);
            UIHelper.showMessageDialog(
                    "login request failed, please check the remote info.");
        }
    }
}
