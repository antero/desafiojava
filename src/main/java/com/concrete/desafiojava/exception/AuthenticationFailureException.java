package com.concrete.desafiojava.exception;

public class AuthenticationFailureException extends RuntimeException {
    public AuthenticationFailureException() {
        super("Invalid username and/or password");
    }
}
