package com.utils.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Log {

    private static Log logInstance = null;
    private static Logger log = LogManager.getLogger();
    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "HH:mm:ss.SSS");
    private static final String OUTPUT_FORMAT = "%s [%s] %s %s%n";
    private LogType type;

    public Log() {
        this.type = LogType.SYSTEM_OUT;
    }

    public synchronized static Log getInstance() {
        if (logInstance == null) {
            logInstance = new Log();
        }
        return logInstance;
    }

    public void info(String msg) {
        output(msg, LogOption.I.getName());
    }

    public void error(String msg) {
        output(msg, LogOption.E.getName());
    }

    public void warn(String msg) {
        output(msg, LogOption.W.getName());
    }

    public void fatal(String msg) {
        output(msg, LogOption.F.getName());
    }

    public void debug(String msg) {
        output(msg, LogOption.D.getName());
    }

    private synchronized void output(String msg, String op) {
        String output = format(msg, op);
        switch (type) {
            case SYSTEM_OUT:
                System.out.print(output);
                break;
            case LOG_OUT:
                log.info(output);
                break;
            case FILE_OUT:
                FileUtils.getInstance().write(output, FileUtils.OUTPUT_PATH);
                break;
            case NON_DEBUG:
                // do nothing.
                break;
        }
    }

    private String format(String msg, String op) {
        return String.format(OUTPUT_FORMAT, formatter.format(new Date()),
                Thread.currentThread().getName(), op, msg);
    }

}
