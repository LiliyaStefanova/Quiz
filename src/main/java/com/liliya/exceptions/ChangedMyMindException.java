package com.liliya.exceptions;


public class ChangedMyMindException extends RuntimeException {

    public ChangedMyMindException() {
    }

    public ChangedMyMindException(String message) {
        super(message);
    }

    public ChangedMyMindException(Throwable cause) {
        super(cause);
    }

    public ChangedMyMindException(String message, Throwable cause) {
        super(message, cause);
    }
}
