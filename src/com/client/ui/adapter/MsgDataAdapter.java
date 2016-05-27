package com.client.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import com.protocal.Activity;

@SuppressWarnings("serial")
public class MsgDataAdapter extends DefaultListModel<Activity> {

    private List<Activity> activities;

    public MsgDataAdapter() {
        this.activities = new ArrayList<>();
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
    
    public void add(Activity ac) {
        if (activities != null) {
            activities.add(ac);
            update();
        }
    }

    private void update() {
        fireContentsChanged(this, 0, getSize() - 1);
    }

}
