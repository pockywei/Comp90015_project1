package com;

import com.base.BaseSubject;
import com.client.ui.LoginFrame;

public class Client extends BaseSubject {

    public static void main(String[] args) {
        log.info("start GUI");
        new LoginFrame();
    }

}
