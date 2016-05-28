package com.client.ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JOptionPane;

import com.client.UserSettings;

public class UIHelper {

    public static final int INPUT_SIZE = 20;
    public static final DialogHeader[] REGISTER_HEADER = {
            new DialogHeader("Username"), new DialogHeader("Password", true),
            new DialogHeader("Password confirm", true) };

    public static DialogHeader[] getRemoteHeader() {
        DialogHeader[] header = {
                new DialogHeader("RemoteHost", UserSettings.getRemoteHost()),
                new DialogHeader("RemotePort",
                        String.valueOf(UserSettings.getRemotePort())) };
        return header;
    }

    /**
     * create a cell for GridBagLayout
     * 
     * @param x
     *            column number
     * @param y
     *            raw number
     * @param margin
     * @return
     */
    public static GridBagConstraints makeGbc(int x, int y, int margin) {
        return makeGbc(x, y, margin, false, 0);
    }

    public static GridBagConstraints makeGbc(int x, int y, int margin,
            int weightx) {
        return makeGbc(x, y, margin, false, weightx);
    }

    public static GridBagConstraints makeGbc(int x, int y, int margin,
            boolean end, int weightx) {
        GridBagConstraints cell = new GridBagConstraints();
        cell.gridwidth = end ? 0 : 1;
        cell.gridheight = 1;
        cell.gridx = x;
        cell.gridy = y;
        cell.weightx = weightx;
        cell.weighty = 1.0;
        cell.insets = new Insets(margin, margin, margin, margin);
        cell.anchor = (x == 0) ? GridBagConstraints.LINE_START
                : GridBagConstraints.LINE_END;
        cell.fill = GridBagConstraints.BOTH;
        return cell;
    }

    public static void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "",
                JOptionPane.PLAIN_MESSAGE, null);
    }
}
