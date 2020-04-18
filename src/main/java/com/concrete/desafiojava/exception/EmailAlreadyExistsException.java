package com.concrete.desafiojava.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) { super(email + " is already taken"); }
}
