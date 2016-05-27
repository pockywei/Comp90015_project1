package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.base.BaseFrame;
import com.client.core.ClientManger;
import com.client.ui.LoginFrame;
import com.client.ui.UIHelper;
import com.client.ui.adapter.MsgDataAdapter;
import com.client.ui.adapter.MsgViewAdapter;
import com.protocal.Activity;
import com.protocal.Command;
import com.protocal.Protocal;
import com.utils.UtilHelper;

public class TestMessageFrame extends BaseFrame {

    private static final long serialVersionUID = 1L;
    private static final int WINDOW_WIDTH = 580;
    private static final int WINDOW_HEIGHT = 700;
    private static final int INPUT_HEIGHT = 80;
    private static final int MARGIN = 5;
    private static final int MESSAGE_VIEW_WIDTH = 280;
    private static final int INDENT_WIDTH = 60;
    private static final int LIST_VISIBLE_COUNT = 10;

    private JTextArea inputText;
    private JButton sendButton;
    private JButton logoutButton;
    private JList<Activity> listView;
    private MsgDataAdapter adapter;

    @Override
    public void initView() {

        JPanel inputPanel = new JPanel();
        placeInputArea(inputPanel);

        add(placeOutputArea(), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.PAGE_END);

        setLocationRelativeTo(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JScrollPane scrollPane = new JScrollPane(inputText);
        scrollPane.setPreferredSize(new Dimension(WINDOW_WIDTH, INPUT_HEIGHT));
        input.add(scrollPane);

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
        listView = new JList<>();
        listView.setCellRenderer(
                new MsgViewAdapter(MESSAGE_VIEW_WIDTH, MARGIN, INDENT_WIDTH));
        adapter = new MsgDataAdapter();
        adapter.add(new Activity("System Announce",
                "Start Chat with your online friends now! Have fun~", true));
        listView.setModel(adapter);
        listView.setVisibleRowCount(LIST_VISIBLE_COUNT);

        JScrollPane outputScroll = new JScrollPane(listView);
        return outputScroll;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                new TestMessageFrame();
            }
        });
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

    private void addMsgView(final Activity ac) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                log.info("add a message onto page");
                // change the message as a formatted object only if the message
                // is a
                // json object
                String message = ac.getMessage();
                ac.setMessage(UtilHelper.getPrettyJson(message));
                adapter.add(ac);
                listView.ensureIndexIsVisible(adapter.getSize() - 1);
            }
        });
    }

    private void sendMessage(String message) {
        try {
            ClientManger.getInstance().sendActivityMessage(message);
            inputText.setText("");
            addMsgView(new Activity(Protocal.SEND_BY_ME, message, true));
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

