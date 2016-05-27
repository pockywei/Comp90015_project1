package com.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.base.BaseFrame;
import com.client.core.ClientManger;
import com.protocal.Activity;
import com.protocal.Command;
import com.protocal.Protocal;
import com.utils.UtilHelper;
import com.utils.log.FileUtils;

public class MessageFrame extends BaseFrame {

    private static final long serialVersionUID = 1L;
    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_HEIGHT = 700;
    private static final int INPUT_WIDTH = 550;
    private static final int INPUT_HEIGHT = 80;
    private static final int MARGIN = 5;

    private JTextArea inputText;
    private JTextArea outputText;
    private JButton sendButton;
    private JButton logoutButton;

    @Override
    public void initView() {

        JPanel inputPanel = new JPanel();

        placeInputArea(inputPanel);

        add(placeOutputArea(), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.PAGE_END);

        setLocationRelativeTo(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a system message
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                addMsgView(new Activity("System Announce",
                        "Start Chat with your online friends now! Have fun~",
                        true));
            }
        });
    }

    private void placeInputArea(JPanel input) {
        input.setLayout(new BoxLayout(input, BoxLayout.PAGE_AXIS));

        Border lineBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.lightGray),
                "Send Messge: (input either a plain message or a json object)");
        input.setBorder(lineBorder);

        inputText = new JTextArea();
        inputText.setLineWrap(true);
        inputText.setWrapStyleWord(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                inputText.requestFocus();
            }
        });

        JScrollPane scrollPane = new JScrollPane(inputText);
        input.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));

        JPanel buttonGroup = new JPanel();
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        logoutButton = new JButton("LogOut");
        logoutButton.addActionListener(this);

        buttonGroup.add(sendButton);
        buttonGroup.add(logoutButton);
        input.add(buttonGroup);
    }

    private JScrollPane placeOutputArea() {
        outputText = new JTextArea();
        outputText.setLineWrap(true);
        outputText.setEditable(false);
        outputText.setWrapStyleWord(true);
        outputText.setBorder(new EmptyBorder(MARGIN, MARGIN, 0, MARGIN));

        JScrollPane outputScroll = new JScrollPane(outputText);
        return outputScroll;
    }

    private void addMsgView(Activity ac) {
        // format message
        ac.setMessage(UtilHelper.getPrettyJson(ac.getMessage()));
        outputText.append(ac.getUsername() + Protocal.RECEIVE_MESSAGE_TAG
                + FileUtils.NEW_LINE);
        outputText.append(ac.getMessage() + FileUtils.NEW_LINE);
        outputText.append(FileUtils.NEW_LINE);
        outputText.setSelectionStart(outputText.getText().length());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sendButton) {
            String msg = inputText.getText().trim().replaceAll("\r", "")
                    .replaceAll("\n", "").replaceAll("\t", "");
            if (UtilHelper.isEmptyStr(msg)) {
                return;
            }
            sendMessage(msg);
        }
        else if (e.getSource() == logoutButton) {
            try {
                ClientManger.getInstance().sendLogoutRequest();
                nextFrame(LoginFrame.class);
            }
            catch (Exception e1) {
                log.error("logout request send failed by the exception " + e1);
                UIHelper.showMessageDialog(
                        "logout request failed, please check the remote info or network.");
            }
        }
    }

    private void sendMessage(String message) {
        try {
            ClientManger.getInstance().sendActivityMessage(message);
            inputText.setText("");
            addMsgView(new Activity(Protocal.SEND_BY_ME, message, false));
        }
        catch (Exception e1) {
            log.error("Activity request send failed by the exception " + e1);
            UIHelper.showMessageDialog(
                    "Activity message request failed, please check the remote info or network.");
        }
    }

    @Override
    public void actionSuccess(Command com, String info, Object o) {
        switch (com) {
            case ACTIVITY_BROADCAST:
                // update the received message to frame.
                addMsgView((Activity) o);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionFailed(Command com, String info) {
        UIHelper.showMessageDialog(info);
    }
}
