package com.client.core.inter;

import com.protocal.Command;

public interface FrameUpdateListener {

    public void actionSuccess(Command com, String info);

    public void actionFailed(String info);
}
