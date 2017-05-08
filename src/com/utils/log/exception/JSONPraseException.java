package com.utils.log.exception;

import com.protocal.Protocal;

public class JSONPraseException extends Exception {

    private static final long serialVersionUID = 1L;

    public JSONPraseException(String command) {
        super(Protocal.ERROR_PARSE + command);
    }
}
