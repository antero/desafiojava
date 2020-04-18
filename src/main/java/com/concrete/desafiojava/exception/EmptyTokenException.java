package com.concrete.desafiojava.exception;

public class EmptyTokenException extends RuntimeException {
    public EmptyTokenException() {
        super("Authentication token must not be empty");
    }
}
