package test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.protocal.Command;
import com.protocal.Protocal;

public class JSONFormatOutput {

    public static void main(String[] args) {
        JsonObject json = new JsonObject();
        json.addProperty(Protocal.COMMAND, Command.LOGIN.name());
        json.addProperty(Protocal.USER_NAME, "zac");
        json.addProperty(Protocal.SECRET, "123");
        String prettyStr = "Me > ";
        prettyStr += new GsonBuilder().setPrettyPrinting().create()
                .toJson(json);

        System.out.println(prettyStr);
        System.out.println("finish");
    }

}
