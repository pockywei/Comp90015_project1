package test;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.beans.UserInfo;

public class TestMap {

    public static void main(String[] args) {
        UserInfo key = new UserInfo("key", "sdfsdf");
        UserInfo vInfo = new UserInfo("1111", "sdfsdf");

        setRootConnection(vInfo, key);

        map.put(key, vInfo);

        UserInfo user = getRootConnection(new UserInfo("key", "sdfsdf"));

        System.out.println(user);

        user = map.get(new UserInfo("key", "sdfsdf"));
        System.out.println(user);
    }

    public static ConcurrentHashMap<UserInfo, UserInfo> rootMap = new ConcurrentHashMap<>();
    public static HashMap<UserInfo, UserInfo> map = new HashMap<>();

    public static void setRootConnection(UserInfo root, UserInfo user) {
        rootMap.put(user, root);
    }

    public static UserInfo getRootConnection(UserInfo user) {
        return rootMap.get(user);
    }
}
