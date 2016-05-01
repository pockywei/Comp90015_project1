package com.client.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.base.BaseFrame;
import com.client.core.ClientManger;
import com.utils.UtilHelper;
import com.utils.log.Log;

public class RegisterDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final Log log = Log.getInstance();
    private JButton OK;
    private JButton Cancel;
    private JTextField userText;
    private JPasswordField passwordText;
    private JPasswordField passwordcfmText;

    public RegisterDialog(BaseFrame parent) {
        super(parent, true);
        setTitle("Register");
        setSize(340, 180);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(4, 2));
        JLabel userLabel = new JLabel("     Username");
        userLabel.setBounds(40, 80, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(140, 80, 160, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("     Password");
        passwordLabel.setBounds(40, 140, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(140, 140, 160, 25);
        panel.add(passwordText);

        JLabel passwordcfmLabel = new JLabel("     Password confirm");
        passwordcfmLabel.setBounds(20, 200, 130, 25);
        panel.add(passwordcfmLabel);

        passwordcfmText = new JPasswordField(20);
        passwordcfmText.setBounds(140, 200, 160, 25);
        panel.add(passwordcfmText);

        OK = new JButton("OK");
        OK.setBounds(140, 240, 60, 40);
        OK.addActionListener(Listener);
        panel.add(OK);
        Cancel = new JButton("Cancel");
        Cancel.setBounds(240, 240, 60, 40);
        Cancel.addActionListener(Listener);
        panel.add(Cancel);

        add(panel);
        setVisible(true);
    }

    private ActionListener Listener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == OK) {
                String user = userText.getText();
                String secret = new String(passwordText.getPassword());
                String re_secret = new String(passwordcfmText.getPassword());
                if (!UtilHelper.isEmptyStr(user)
                        && !UtilHelper.isEmptyStr(secret)
                        && !UtilHelper.isEmptyStr(re_secret)) {
                    if (secret.equals(re_secret)) {
                        try {
                            ClientManger.getInstance().sendRegisterRequest(user,
                                    secret);
                            close();
                        }
                        catch (Exception e1) {
                            log.error(
                                    "register request send failed by the exception "
                                            + e1);
                            JOptionPane.showMessageDialog(null,
                                    "register request failed, please check the remote info.");
                        }
                        return;
                    }
                    JOptionPane.showMessageDialog(null,
                            "the passwords are not equal, please try again.");
                    return;
                }
                JOptionPane.showMessageDialog(null,
                        "username or password can not be empty, please try again.");
            }
            else if (e.getSource() == Cancel) {
                close();
            }
        }
    };

    public void close() {
        setVisible(false);
        dispose();
    }
}
