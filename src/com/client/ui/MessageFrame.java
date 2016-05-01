package com.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.base.BaseFrame;
import com.client.core.ClientManger;
import com.protocal.Command;
import com.protocal.Protocal;
import com.utils.UtilHelper;
import com.utils.log.FileUtils;

public class MessageFrame extends BaseFrame {

    private static final long serialVersionUID = 1L;
    private JTextArea inputText;
    private JTextArea outputText;
    private JButton sendButton;
    private JButton logoutButton;
    private JScrollPane outputScroll;
    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_HEIGHT = 700;
    private static final int INPUT_WIDTH = 550;
    private static final int INPUT_HEIGHT = 80;
    private static final String STAR = "=";
    private static final int SPLIT_LINE = 50;

    @Override
    public void initView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        JPanel inputPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));

        outputPanel.setLayout(new BorderLayout());

        Border lineBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.lightGray),
                "Send Messge:");
        inputPanel.setBorder(lineBorder);

        lineBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.lightGray),
                "Received from server");
        outputPanel.setBorder(lineBorder);

        inputText = new JTextArea();

        inputText.setLineWrap(true);
        inputText.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(inputText);

        inputPanel.add(scrollPane);
        inputPanel.setSize(INPUT_WIDTH, INPUT_HEIGHT);
        scrollPane.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));

        JPanel buttonGroup = new JPanel();
        sendButton = new JButton("Send");
        logoutButton = new JButton("LogOut");
        buttonGroup.add(sendButton);
        buttonGroup.add(logoutButton);

        inputPanel.add(buttonGroup);
        sendButton.addActionListener(this);
        logoutButton.addActionListener(this);

        outputText = new JTextArea();
        outputText.setEditable(false);
        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        outputScroll = new JScrollPane(outputText);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        mainPanel.add(outputPanel);
        mainPanel.add(inputPanel);

        add(mainPanel);

        setLocationRelativeTo(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                JOptionPane.showMessageDialog(null,
                        "logout request failed, please check the remote info or network.");
            }
        }
    }

    private void sendMessage(String message) {
        try {
            ClientManger.getInstance().sendActivityMessage(message);
            inputText.setText("");
            outputText.append(String.format(Protocal.SEND_MESSAGE,
                    Protocal.SEND_BY_ME, message) + FileUtils.NEW_LINE);
            outputText.append(FileUtils.NEW_LINE);
            outputText.setSelectionStart(outputText.getText().length());
        }
        catch (Exception e1) {
            log.error("Activity request send failed by the exception " + e1);
            JOptionPane.showMessageDialog(null,
                    "Activity message request failed, please check the remote info or network.");
        }
    }

    private void receiveMessage(String message) {
        outputText.append(message + FileUtils.NEW_LINE);
        StringBuilder splitLine = new StringBuilder();
        for (int i = 0; i < SPLIT_LINE; i++) {
            splitLine.append(STAR);
        }
        outputText.append(splitLine.toString() + FileUtils.NEW_LINE);
        outputText.setSelectionStart(outputText.getText().length());
    }

    @Override
    public void actionSuccess(Command com, String info) {
        switch (com) {
            case ACTIVITY_BROADCAST:
                // update the received message to frame.
                receiveMessage(info);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionFailed(String info) {
        JOptionPane.showMessageDialog(null, info);
    }
}
