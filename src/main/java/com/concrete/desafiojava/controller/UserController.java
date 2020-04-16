package com.concrete.desafiojava.controller;

import com.concrete.desafiojava.exception.UserNotFoundException;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public List<User> list() {
        return repository.findAll();
    }

    @PostMapping("/cadastro")
    public User create(@RequestBody User newUser) {
        User user = repository.save(newUser);
        user.setLastLogin(user.getCreated());
        return repository.save(user);
    }

    @GetMapping("users/{id}")
    public User retrieve(@PathVariable Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new UserNotFoundException(id));
    }
}
