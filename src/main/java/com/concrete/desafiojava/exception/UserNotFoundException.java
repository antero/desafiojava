package com.concrete.desafiojava.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Could not find user " + id);
    }
    public UserNotFoundException(String name) {
        super("Could not find user " + name);
    }
}
