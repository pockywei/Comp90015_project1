package com;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.base.BaseSubject;
import com.server.ServerSettings;
import com.server.core.ServerManager;
import com.utils.UtilHelper;

public class Server extends BaseSubject {

    private static Options options;

    static {
        options = new Options();
        options.addOption("lp", true, "local port number");
        options.addOption("rp", true, "remote port number");
        options.addOption("rh", true, "remote hostname");
        options.addOption("lh", true, "local hostname");
        options.addOption("a", true, "activity interval in milliseconds");
        options.addOption("s", true, "secret for the server to use");
    }

    public static void main(String[] args) {
        log.info("reading command line options");
        // build the parser
        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(options, args);
        }
        catch (ParseException e1) {
            help(options);
        }

        // get local host name
        String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            log.warn("failed to get localhost IP address");
        }

        if (cmd.hasOption("lh")) {
            localHost = cmd.getOptionValue("lh");
        }

        if (UtilHelper.isEmptyStr(localHost)) {
            log.info("-lh requires local host name, parsed: "
                    + cmd.getOptionValue("lh"));
            help(options);
        }

        // get local port number
        int localPort = 0;
        if (cmd.hasOption("lp")) {
            try {
                localPort = Integer.parseInt(cmd.getOptionValue("lp"));
            }
            catch (NumberFormatException e) {
                log.info("-lp requires a port number, parsed: "
                        + cmd.getOptionValue("lp"));
                help(options);
            }
        }
        else {
            log.info("-lp requires a port number, parsed: "
                    + cmd.getOptionValue("lp"));
            help(options);
        }

        // get remote host name
        String remoteHost = null;
        if (cmd.hasOption("rh")) {
            remoteHost = cmd.getOptionValue("rh");
        }

        // get remote host port number
        int remotePort = 0;
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

        // get activity interval time
        int activityInterval = 0;
        if (cmd.hasOption("a")) {
            try {
                activityInterval = Integer.parseInt(cmd.getOptionValue("a"));
            }
            catch (NumberFormatException e) {
                log.error("-a requires a number in milliseconds, parsed: "
                        + cmd.getOptionValue("a"));
                help(options);
            }
        }

        String remoteSecret = null;
        if (cmd.hasOption("s")) {
            remoteSecret = cmd.getOptionValue("s");
        }

        ServerSettings.setInfo(localPort, localHost, remotePort, remoteHost,
                remoteSecret, activityInterval);
        log.info("starting server");
        ServerManager.getInstance();
    }

}
