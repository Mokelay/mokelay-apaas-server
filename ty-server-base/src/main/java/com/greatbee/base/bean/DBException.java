package com.greatbee.base.bean;

/**
 * DB Exception
 * Created by CarlChen on 2017/5/26.
 */
public class DBException extends Exception {
    private long code;

    public DBException(long code) {
        this.code = code;
    }

    public DBException(String message, long code) {
        super(message);
        this.code = code;
    }

    public DBException(String message, Throwable cause, long code) {
        super(message, cause);
        this.code = code;
    }

    public DBException(Throwable cause, long code) {
        super(cause);
        this.code = code;
    }

    public DBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, long code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
