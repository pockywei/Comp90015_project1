package test;

import java.util.concurrent.ConcurrentHashMap;

import com.beans.UserInfo;
import com.server.core.listener.LockState;

public class TestLockstate {

    private static LockState[] list = LockState.initLockStateList(0);
    private static ConcurrentHashMap<Integer, LockState[]> registerMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        registerMap.put(1, list);

        System.out.println(reduceCount(false, 1));
        System.out.println("return : " + hasFinishLock(1));
        print(1);
        System.out.println(reduceCount(false, 1));
        System.out.println("return : " + hasFinishLock(1));
        print(1);
        System.out.println(reduceCount(true, 1));
        System.out.println("return : " + hasFinishLock(1));
        print(1);
    }

    public static int reduceCount(boolean isAllow, int key) {
        LockState[] list = registerMap.get(key);

        int waitCount = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] == LockState.WAITTED) {
                list[i] = isAllow ? LockState.ALLOWED : LockState.DENIED;
                break;
            }
        }
        final LockState[] list1 = registerMap.get(key);
        for (LockState s : list1) {
            if (s == LockState.WAITTED)
                waitCount++;
        }
        return waitCount;
    }

    public static LockState hasFinishLock(int key) {
        final LockState[] states = registerMap.get(key);
        if (states == null) {
            return null;
        }
        boolean isAllow = true;
        for (LockState s : states) {
            if (s == LockState.WAITTED) {
                return LockState.WAITTED;
            }
            if (s == LockState.DENIED) {
                isAllow = false;
            }
        }
        // wait process finish, remove the waiting record
        registerMap.remove(key);
        return isAllow ? LockState.ALLOWED : LockState.DENIED;
    }

    private static void print(int key) {
        LockState[] list = registerMap.get(key);
        if (list != null) {
            for (LockState s : list) {
                System.out.println(s);
            }
        }

        System.out.println("map size : " + registerMap.size());
        System.out.println();
    }
}
