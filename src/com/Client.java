package com;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.base.BaseSubject;
import com.client.UserSettings;
import com.client.ui.LoginFrame;
import com.utils.UtilHelper;

public class Client extends BaseSubject {

    private static Options options;

    static {
        options = new Options();
        options.addOption("u", true, "username");
        options.addOption("rp", true, "remote port number");
        options.addOption("rh", true, "remote hostname");
        options.addOption("s", true, "secret for username");
    }

    public static void main(String[] args) {
        readCommand(args);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                log.info("start GUI");
                new LoginFrame();
            }
        });
    }

    private static void readCommand(String[] args) {
        if (args == null || args.length == 0) {
            log.info(
                    "-rh or -rp is missing. Please click : [Menu -> RemoteSever] to add remote info by using GUI frame.");
            return;
        }
        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(options, args);
        }
        catch (ParseException e1) {
            help(options);
        }

        if (!cmd.hasOption("rh") || !cmd.hasOption("rp")) {
            log.info(
                    "-rh or -rp is missing. Please click : [Menu -> RemoteSever] to add remote info by using GUI frame.");
            return;
        }

        // get remote info
        String remoteHost = "";
        int remotePort = 0;

        remoteHost = cmd.getOptionValue("rh");
        if (UtilHelper.isEmptyStr(remoteHost)) {
            log.error("the remote hostname can not be empty.");
            help(options);
        }

        try {
            remotePort = Integer.parseInt(cmd.getOptionValue("rp"));
        }
        catch (NumberFormatException e) {
            log.error("-rp requires a port number, parsed: "
                    + cmd.getOptionValue("rp"));
            help(options);
        }

        UserSettings.setServerInfo(remotePort, remoteHost);
        log.info("remote info has been added : " + remoteHost + ":"
                + remotePort);

        if (cmd.hasOption("s") || cmd.hasOption("u")) {
            log.info(
                    "Please use GUI frame to type into the username for -u and secret for -s.");
        }
    }
}
