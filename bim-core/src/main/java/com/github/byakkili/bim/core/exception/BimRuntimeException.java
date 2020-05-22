package com.github.byakkili.bim.core.exception;

/**
 * @author Guannian Li
 */
public class BimRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BimRuntimeException() {
        super();
    }

    public BimRuntimeException(String message) {
        super(message);
    }

    public BimRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BimRuntimeException(Throwable cause) {
        super(cause);
    }

    protected BimRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
