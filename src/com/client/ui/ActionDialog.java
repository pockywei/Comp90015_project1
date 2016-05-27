package com.client.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.base.BaseFrame;
import com.utils.UtilHelper;

public class ActionDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 340;
    private static final int FRAME_HEIGHT = 70;
    private static final int FRAME_MIN_HEIGHT = 180;
    private static final int MARGIN = 10;
    private static final int INDENT_WIDTH = 20;
    private JButton OK;
    private JButton Cancel;
    private ActionDialogListener actionlistener;
    private List<JTextField> fields;
    private DialogHeader[] headers;

    public ActionDialog(BaseFrame parent) {
        this(parent, "Progress");
        setSize(FRAME_WIDTH - 4 * INDENT_WIDTH, FRAME_MIN_HEIGHT / 2);
        setLocationRelativeTo(parent);
        add(initProgressBar());
    }

    private JPanel initProgressBar() {
        JPanel panel = new JPanel();
        panel.setBorder(
                new EmptyBorder(MARGIN, INDENT_WIDTH, MARGIN, INDENT_WIDTH));
        // progress bar
        URL url = getClass().getResource("loading.gif");
        Icon icon = new ImageIcon(url);
        final JLabel label = new JLabel(icon);
        final JLabel text = new JLabel("connecting...");

        panel.add(label);
        panel.add(text);
        return panel;
    }

    public ActionDialog(BaseFrame parent, String title, DialogHeader[] headers,
            ActionDialogListener actionlistener) {
        this(parent, title);
        this.actionlistener = actionlistener;
        this.headers = headers;
        fields = new ArrayList<>();
        int height = headers.length * FRAME_HEIGHT;
        setSize(FRAME_WIDTH,
                height < FRAME_MIN_HEIGHT ? FRAME_MIN_HEIGHT : height);
        setLocationRelativeTo(parent);
        add(initView());
        setVisible(true);
    }

    public ActionDialog(BaseFrame parent, String title) {
        super(parent, true);
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private JPanel initView() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(
                new EmptyBorder(MARGIN, INDENT_WIDTH, MARGIN, INDENT_WIDTH));

        JPanel input = new JPanel();
        input.setLayout(new GridBagLayout());
        panel.add(input, BorderLayout.CENTER);

        JPanel action = new JPanel();
        panel.add(action, BorderLayout.SOUTH);

        placeContents(input);
        placeButtons(action);
        return panel;
    }

    private void placeContents(JPanel parent) {
        for (int i = 0; i < headers.length; i++) {
            JLabel label = new JLabel(headers[i].getName());
            GridBagConstraints cell = UIHelper.makeGbc(0, i, 0);
            parent.add(label, cell);

            JTextField text = null;
            if (headers[i].isPassword()) {
                text = new JPasswordField(UIHelper.INPUT_SIZE);
            }
            else {
                text = new JTextField(UIHelper.INPUT_SIZE);
            }

            if (!UtilHelper.isEmptyStr(headers[i].getText())) {
                text.setText(headers[i].getText());
            }
            cell = UIHelper.makeGbc(1, i, 0, 1);
            parent.add(text, cell);
            fields.add(text);
        }
    }

    private void placeButtons(JPanel parent) {
        OK = new JButton("OK");
        OK.addActionListener(listener);
        parent.add(OK);

        Cancel = new JButton("Cancel");
        Cancel.addActionListener(listener);
        parent.add(Cancel);
    }

    private ActionListener listener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == OK) {
                if (actionlistener != null) {
                    actionlistener.confirmPerformed(ActionDialog.this, fields);
                }
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

    public interface ActionDialogListener {
        public void confirmPerformed(ActionDialog dialog,
                List<JTextField> fields);
    }
}
