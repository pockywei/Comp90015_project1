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
        output(msg, LogOption.I);
    }

    public void error(String msg) {
        output(msg, LogOption.E);
    }

    public void warn(String msg) {
        output(msg, LogOption.W);
    }

    public void fatal(String msg) {
        output(msg, LogOption.F);
    }

    public void debug(String msg) {
        output(msg, LogOption.D);
    }

    private synchronized void output(String msg, LogOption op) {
        String output = format(msg, op);
        switch (type) {
            case SYSTEM_OUT:
                System.out.print(output);
                break;
            case LOG_OUT:
                switch (op) {
                    case I:
                        log.info(output);
                        break;
                    case E:
                        log.error(output);
                        break;
                    case W:
                        log.warn(output);
                        break;
                    case F:
                        log.fatal(output);
                        break;
                    case D:
                        log.debug(output);
                        break;
                }
                break;
            case FILE_OUT:
                FileUtils.getInstance().write(output, FileUtils.OUTPUT_PATH);
                break;
            case NON_DEBUG:
                // do nothing.
                break;
        }
    }

    private String format(String msg, LogOption op) {
        return String.format(OUTPUT_FORMAT, formatter.format(new Date()),
                Thread.currentThread().getName(), op, msg);
    }

}
