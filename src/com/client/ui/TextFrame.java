package com.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.BoxLayout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

// @SuppressWarnings("serial")
public class TextFrame extends JFrame implements ActionListener {
    private static final Logger log = LogManager.getLogger();
    private JTextArea inputText;
    private JTextArea outputText;
    private JButton sendButton;
    private JButton disconnectButton;
    private JSONParser parser = new JSONParser();

    public TextFrame() {
        setTitle("ActivityStreamer Text I/O");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        // mainPanel.setLayout(new GridLayout(2, 2));

        JPanel inputPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
        // inputPanel.setLayout(new BorderLayout());
        outputPanel.setLayout(new BorderLayout());
        Border lineBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.lightGray),
                "JSON input, to send to server");
        inputPanel.setBorder(lineBorder);
        lineBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.lightGray),
                "JSON output, received from server");
        // outputPanel.setBorder(lineBorder);
        outputPanel.setName("Text outputs");

        inputText = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(inputText);
        // inputPanel.add(scrollPane, BorderLayout.CENTER);
        inputPanel.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(300, 40));

        JPanel buttonGroup = new JPanel();
        sendButton = new JButton("Send");
        disconnectButton = new JButton("Disconnect");
        buttonGroup.add(sendButton);
        buttonGroup.add(disconnectButton);
        // inputPanel.add(buttonGroup,BorderLayout.SOUTH);
        inputPanel.add(buttonGroup);
        sendButton.addActionListener(this);
        disconnectButton.addActionListener(this);

        outputText = new JTextArea();
        JScrollPane scrollPane2 = new JScrollPane(outputText);
        outputPanel.add(scrollPane2, BorderLayout.CENTER);

        mainPanel.add(outputPanel);
        mainPanel.add(inputPanel);

        add(mainPanel);

        setLocationRelativeTo(null);
        setSize(300, 888);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setOutputText(final JSONObject obj) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(obj.toJSONString());
        String prettyJsonString = gson.toJson(je);
        outputText.setText(prettyJsonString);
        outputText.revalidate();
        outputText.repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sendButton) {
            String msg = inputText.getText().trim().replaceAll("\r", "")
                    .replaceAll("\n", "").replaceAll("\t", "");
            JSONObject obj;
            try {
                obj = (JSONObject) parser.parse(msg);
                // ClientSolution.getInstance().sendActivityObject(obj);
            }
            catch (ParseException e1) {
                log.error(
                        "invalid JSON object entered into input text field, data not sent");
            }

        }
        else if (e.getSource() == disconnectButton) {
            // ClientSolution.getInstance().disconnect();
        }

    }

    public static void main(String[] args) {
        TextFrame frame = new TextFrame();
    }
}
