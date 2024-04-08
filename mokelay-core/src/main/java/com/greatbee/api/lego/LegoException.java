package com.greatbee.api.lego;

/**
 * Lego Exception
 *
 * Created by ty on 2017/7/9.
 */
public class LegoException extends  Exception {
    private long code;

    public LegoException(long code) {
        this.code = code;
    }

    public LegoException(String message, long code) {
        super(message);
        this.code = code;
    }

    public LegoException(String message, Throwable cause, long code) {
        super(message, cause);
        this.code = code;
    }

    public LegoException(Throwable cause, long code) {
        super(cause);
        this.code = code;
    }

    public LegoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, long code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
