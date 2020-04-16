package com.concrete.desafiojava.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

@Service
public class JWTServiceImpl implements JWTService {

    @Override
    public String create(String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                  .withIssuer("auth0")
                  .sign(algorithm);
    }

    @Override
    public boolean verify(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer("auth0")
                                      .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }
}
