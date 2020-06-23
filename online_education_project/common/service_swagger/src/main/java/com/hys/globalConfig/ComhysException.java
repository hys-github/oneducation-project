package com.hys.globalConfig;

/**
 * @Auth 86191
 * @Date 2020/4/23
 */
public class ComhysException extends RuntimeException {

    public ComhysException() {
        super();
    }

    public ComhysException(String message) {
        super(message);
    }

    public ComhysException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComhysException(Throwable cause) {
        super(cause);
    }

    protected ComhysException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
