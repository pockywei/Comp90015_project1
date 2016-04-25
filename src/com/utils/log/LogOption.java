package com.utils.log;

public enum LogOption {
    I("INFO"), E("ERROR"), W("WARN"), F("FATAL"), D("DEBUG");

    private String name;

    LogOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
