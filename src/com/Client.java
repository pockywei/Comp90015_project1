package com;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.base.BaseSubject;
import com.client.UserSettings;

public class Client extends BaseSubject {

    private static Options options;

    static {
        Options options = new Options();
        options.addOption("u", true, "username");
        options.addOption("rp", true, "remote port number");
        options.addOption("rh", true, "remote hostname");
        options.addOption("s", true, "secret for username");
    }

    public static void main(String[] args) {
        log.info("reading command line options");
        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(options, args);
        }
        catch (ParseException e1) {
            help(options);
        }

        String user, secret, remoteHost;
        user = secret = remoteHost = null;
        int remotePort = 0;

        if (cmd.hasOption("rh")) {
            remoteHost = cmd.getOptionValue("rh");
        }

        if (cmd.hasOption("rp")) {
            try {
                remotePort = Integer.parseInt(cmd.getOptionValue("rp"));
            }
            catch (NumberFormatException e) {
                log.error("-rp requires a port number, parsed: "
                        + cmd.getOptionValue("rp"));
                help(options);
            }
        }

        if (cmd.hasOption("s")) {
            secret = cmd.getOptionValue("s");
        }

        if (cmd.hasOption("u")) {
            user = cmd.getOptionValue("u");
        }

        UserSettings.setUser(user, secret, remoteHost, remotePort);
        log.info("starting client");

    }
}
