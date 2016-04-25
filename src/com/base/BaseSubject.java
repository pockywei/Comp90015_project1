package com.base;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.utils.log.Log;

public class BaseSubject {
    protected static final Log log = Log.getInstance();

    protected static void help(Options options) {
        String header = "An ActivityStream Server for Unimelb COMP90015\n\n";
        String footer = "\ncontact aharwood@unimelb.edu.au for issues.";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ActivityStreamer.Server", header, options, footer,
                true);
        System.exit(-1);
    }
}
