package com.server.core.response;

public enum LockState {
    ALLOWED, DENIED, WAITTED;

    public static LockState[] initLockStateList(int size) {
        LockState[] list = new LockState[size];
        for (int i = 0; i < size; i++) {
            list[i] = LockState.WAITTED;
        }
        return list;
    }
}
