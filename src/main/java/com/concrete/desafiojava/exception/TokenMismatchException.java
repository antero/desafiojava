package com.concrete.desafiojava.exception;

public class TokenMismatchException extends RuntimeException {
    public TokenMismatchException() {
        super("Authentication tokens don't match");
    }
}
