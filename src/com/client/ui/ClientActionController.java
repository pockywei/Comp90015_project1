package com.client.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientActionController {

    public void listenConnect(JMenuItem con) {

        con.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JTextField xField = new JTextField(6);
                JTextField yField = new JTextField(6);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new GridLayout(2, 0));
                // myPanel.setSize(900, 900);
                JLabel hostname = new JLabel("Hostname");

                // hostname.setBounds(20, 10, 80, 25);
                myPanel.add(hostname);
                myPanel.add(xField);
                // myPanel.add(Box.createHorizontalStrut(10)); // a spacer

                JLabel portnumber = new JLabel("Port No.");
                // portnumber.setBounds(50, 10, 80, 25);
                myPanel.add(portnumber);
                myPanel.add(yField);

                // String hostname = (String)JOptionPane.showInputDialog(null,
                // "Input host name: ", "Input host name",
                // JOptionPane.PLAIN_MESSAGE);
                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter X and Y Values",
                        JOptionPane.OK_CANCEL_OPTION);
            }
        });

    }

    public void listenDisConnect(JMenuItem Discon) {

        Discon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null, "clicked Disconnect");
            }
        });

    }

    public void about(JMenuItem abt) {

        abt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                        "<html><center>COMP90015 PROJECT1 VER 1.0<br>by Eyebrow</center></html>");
            }
        });

    }

    public void ListenLoginButton(JButton login_button) {

        login_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null, "login button");
            }
        });

    }

    public void ListenregisterButton(JButton register_Button) {

        register_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFrame frame = new JFrame("Register");
                frame.setSize(340, 180);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel panel = new JPanel();

                panel.setLayout(new GridLayout(4, 2));
                JLabel userLabel = new JLabel("     Username");
                userLabel.setBounds(40, 80, 80, 25);
                panel.add(userLabel);

                JTextField userText = new JTextField(20);
                userText.setBounds(140, 80, 160, 25);
                panel.add(userText);

                JLabel passwordLabel = new JLabel("     Password");
                passwordLabel.setBounds(40, 140, 80, 25);
                panel.add(passwordLabel);

                JPasswordField passwordText = new JPasswordField(20);
                passwordText.setBounds(140, 140, 160, 25);
                panel.add(passwordText);

                JLabel passwordcfmLabel = new JLabel("     Password confirm");
                passwordcfmLabel.setBounds(20, 200, 130, 25);
                panel.add(passwordcfmLabel);

                JPasswordField passwordcfmText = new JPasswordField(20);
                passwordcfmText.setBounds(140, 200, 160, 25);
                panel.add(passwordcfmText);

                JButton OK = new JButton("OK");
                OK.setBounds(140, 240, 60, 40);
                panel.add(OK);
                JButton Cancel = new JButton("Cancel");
                Cancel.setBounds(240, 240, 60, 40);
                panel.add(Cancel);

                frame.add(panel);
                frame.setVisible(true);

            }
        });

    }

}
