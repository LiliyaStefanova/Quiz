package com.liliya.exceptions;


public class NoQuizException extends RuntimeException {

    public NoQuizException(){}

    public NoQuizException(String message){
        super(message);
    }

    public NoQuizException(Throwable cause){
        super(cause);
    }

    public NoQuizException(String message, Throwable cause){
        super(message, cause);
    }

}
