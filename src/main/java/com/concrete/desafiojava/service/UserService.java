package com.concrete.desafiojava.service;

import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.model.UserLoginDetails;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    User create(User newUser);

    User findByEmail(String email);

    User findById(UUID id);

    List<User> findAll();

    User login(UserLoginDetails details);

    User profile(UUID id, Map<String, Object> payload);
}
