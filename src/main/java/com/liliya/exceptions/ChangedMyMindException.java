package com.liliya.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 16/04/14
 * Time: 23:14
 * To change this template use File | Settings | File Templates.
 */
public class ChangedMyMindException extends RuntimeException {

    public ChangedMyMindException(){}

    public ChangedMyMindException(String message){
        super(message);
    }

    public ChangedMyMindException(Throwable cause){
        super(cause);
    }

    public ChangedMyMindException(String message, Throwable cause){
        super(message, cause);
    }
}
