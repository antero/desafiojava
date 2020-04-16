package com.concrete.desafiojava.service;

public interface JWTService {
    public String create(String secret);

    public boolean verify(String token, String secret);
}
