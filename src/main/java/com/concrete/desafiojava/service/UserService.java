package com.concrete.desafiojava.service;

import com.concrete.desafiojava.model.User;

import java.util.List;

public interface UserService {
    User create(User newUser);

    User findByUsername(String username);

    User findById(Long id);

    List<User> findAll();
}
