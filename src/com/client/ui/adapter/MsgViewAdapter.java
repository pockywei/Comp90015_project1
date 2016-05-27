package com.client.ui.adapter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.protocal.Activity;
import com.protocal.Protocal;

@SuppressWarnings("serial")
public class MsgViewAdapter extends DefaultListCellRenderer {

    private final JPanel leftView = new JPanel(new BorderLayout());
    private final JPanel leftProfileView = new JPanel(new BorderLayout());
    private final JLabel leftNameView = new JLabel();
    private final JLabel leftTextView = new JLabel();

    private final JPanel rightView = new JPanel(new BorderLayout());
    private final JPanel rightProfileView = new JPanel(new BorderLayout());
    private final JLabel rightNameView = new JLabel();
    private final JLabel rightTextView = new JLabel();

    private final String pre = "<html><body style='width: %dpx;'>%s";
    private final String rightPre = "<html><div align=right width=%dpx>%s</div></html>";
    private final int width;

    public MsgViewAdapter(int width, int margin, int indent) {
        this.width = width;
        leftProfileView.add(leftNameView, BorderLayout.NORTH);
        leftProfileView.setBackground(Color.WHITE);
        leftView.add(leftProfileView, BorderLayout.WEST);
        leftView.add(leftTextView, BorderLayout.CENTER);
        leftView.setBorder(new EmptyBorder(margin, margin, margin, indent));
        leftView.setBackground(Color.WHITE);

        rightProfileView.add(rightNameView, BorderLayout.NORTH);
        rightProfileView.setBackground(Color.WHITE);
        rightView.add(rightProfileView, BorderLayout.EAST);
        rightView.add(rightTextView, BorderLayout.CENTER);
        rightView.setBorder(new EmptyBorder(margin, indent, margin, margin));
        rightView.setBackground(Color.WHITE);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        Activity ac = (Activity) value;
        if (ac.isLeft()) {
            leftNameView
                    .setText(ac.getUsername() + Protocal.RECEIVE_MESSAGE_TAG);
            leftTextView.setText(String.format(pre, width, ac.getMessage()));
            return leftView;
        }

        rightNameView.setText(Protocal.SEND_MESSAGE_TAG + ac.getUsername());
        rightTextView.setText(String.format(rightPre, width, ac.getMessage()));
        return rightView;
    }

}