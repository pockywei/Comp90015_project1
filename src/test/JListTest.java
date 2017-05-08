package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.client.ui.adapter.MsgViewAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.protocal.Activity;
import com.protocal.Command;
import com.protocal.Protocal;
import com.utils.UtilHelper;

public class JListTest extends JFrame {

    private static final long serialVersionUID = 1L;
    static JListTest myFrame;
    static int countMe = 0;

    public static int MARGIN = 20;
    public static int INDENT_WIDTH = 80;
    static int WIDTH = 600;
    static int HEIGHT = 200;
    static int MESSAGE_WIDTH = 300;
    JScrollPane outputScroll;
    JList<Activity> listView;
    List<Activity> activities = new ArrayList<>();
    ListDataAdapter adapter;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        myFrame = new JListTest();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.prepareUI();
        myFrame.pack();
        myFrame.setVisible(true);
    }

    public static final String longStr = "This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. This is an even longer text. ";

    private void prepareUI() {
        JPanel frame = new JPanel();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLayout(new BorderLayout());

        listView = new JList<>();
        listView.setCellRenderer(
                new MsgViewAdapter(MESSAGE_WIDTH, MARGIN, INDENT_WIDTH));
        // activities.add(new Activity(true, "user : " + activities.size(),
        // "message : " + activities.size() + "sj"));

        adapter = new ListDataAdapter(activities);
        listView.setModel(adapter);
        // listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listView.setVisibleRowCount(10);

        outputScroll = new JScrollPane(listView);

        JButton buttonAdd = new JButton("Add subPanel");
        buttonAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                String message = UtilHelper.toHTML(getMessage());
                activities.add(new Activity("user : " + activities.size(),
                        longStr, true));
                adapter.update();
                listView.ensureIndexIsVisible(adapter.getSize() - 1);
            }
        });

        JButton buttonRemoveAll = new JButton("Remove All");
        buttonRemoveAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessage();
                System.out.println(message);
                message = UtilHelper.toHTML(message);
                System.out.println(message);
                activities.add(new Activity("user : " + activities.size(),
                        message, false));
                adapter.update();
                listView.ensureIndexIsVisible(adapter.getSize() - 1);
            }
        });

        frame.add(buttonAdd, BorderLayout.PAGE_START);
        frame.add(buttonRemoveAll, BorderLayout.PAGE_END);
        frame.add(outputScroll, BorderLayout.CENTER);

        getContentPane().add(frame, BorderLayout.CENTER);
        setSize(WIDTH, HEIGHT);
    }

    private static String getMessage() {
        JsonObject json = new JsonObject();
        json.addProperty(Protocal.COMMAND, Command.LOGIN.name());
        json.addProperty(Protocal.USER_NAME, "zac");
        json.addProperty(Protocal.SECRET, "123");

        return new GsonBuilder().setPrettyPrinting().create().toJson(json);
    }
}

class ListViewAdapter extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;
    final JPanel leftView = new JPanel(new BorderLayout());
    final JPanel leftProfileView = new JPanel(new BorderLayout());
    final JLabel leftNameView = new JLabel();
    final JLabel leftTextView = new JLabel();

    final JPanel rightView = new JPanel(new BorderLayout());
    final JPanel rightProfileView = new JPanel(new BorderLayout());
    final JLabel rightNameView = new JLabel();
    final JLabel rightTextView = new JLabel();

    final String pre = "<html><body style='width: %dpx;'>%s";
    final String rightPre = "<html><div align=right width=%dpx>%s</div></html>";
    final int width;

    public ListViewAdapter(int width, int margin, int indent) {
        this.width = width;
        leftProfileView.add(leftNameView, BorderLayout.NORTH);
        leftView.add(leftProfileView, BorderLayout.WEST);
        leftView.add(leftTextView, BorderLayout.CENTER);
        leftView.setBorder(new EmptyBorder(margin, margin, margin, indent));

        rightProfileView.add(rightNameView, BorderLayout.NORTH);
        rightView.add(rightProfileView, BorderLayout.EAST);
        rightView.add(rightTextView, BorderLayout.CENTER);
        rightView.setBorder(new EmptyBorder(margin, indent, margin, margin));
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        Activity ac = (Activity) value;
        if (ac.isLeft()) {
            leftNameView.setText(ac.getUsername());
            leftTextView.setText(String.format(pre, width, ac.getMessage()));
            return leftView;
        }

        rightNameView.setText(ac.getUsername());
        rightTextView.setText(String.format(rightPre, width, ac.getMessage()));
        return rightView;
    }

}

class ListDataAdapter extends DefaultListModel<Activity> {

    private static final long serialVersionUID = 1L;
    private List<Activity> activities;

    public ListDataAdapter(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public int getSize() {
        if (activities != null) {
            return activities.size();
        }
        return 0;
    }

    @Override
    public Activity getElementAt(int index) {
        if (activities != null) {
            return activities.get(index);
        }
        return null;
    }

    public void update() {
        fireContentsChanged(this, 0, getSize() - 1);
    }

}
