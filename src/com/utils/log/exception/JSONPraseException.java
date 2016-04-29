package com.utils.log.exception;

import com.protocal.Protocal;

public class JSONPraseException extends Exception {

    public JSONPraseException(String command) {
        super(Protocal.ERROR_PARSE + command);
    }
}
