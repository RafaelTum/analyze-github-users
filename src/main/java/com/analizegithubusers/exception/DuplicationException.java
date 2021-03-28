package com.analizegithubusers.exception;

public class DuplicationException extends RuntimeException {

    public DuplicationException() {
    }

    public DuplicationException(String message) {
        super(message);
    }

    public DuplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}