package com.schoolmanagement.schoolmanagement.exception;

import java.util.List;

public class BadRequestException extends Exception {

    private List<?> errors;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    protected BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BadRequestException(String message, List<?> errors) {
        super(message);
        this.errors = errors;
    }

    public List<?> getErrors() {
        return errors;
    }
}
