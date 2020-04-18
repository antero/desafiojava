package com.concrete.desafiojava.exception;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String time) { super("Session time exceeded " + time); }
}
