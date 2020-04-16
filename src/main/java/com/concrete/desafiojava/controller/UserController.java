package com.concrete.desafiojava.controller;

import com.concrete.desafiojava.exception.UserNotFoundException;
import com.concrete.desafiojava.model.PhoneNumber;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.repository.PhoneNumberRepository;
import com.concrete.desafiojava.repository.UserRepository;
import com.concrete.desafiojava.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private PhoneNumberRepository phoneRepository;

    @GetMapping("/users")
    public List<User> list() {
        return repository.findAll();
    }

    @PostMapping("/cadastro")
    public User create(@RequestBody User newUser) {
        List<PhoneNumber> phones = newUser.getPhones();
        newUser.setPhones(null);
        User user = repository.save(newUser);
        user.setPhones(phoneRepository.saveAll(phones));
        user.setLastLogin(user.getCreated());
        return repository.save(user);
    }

    @GetMapping("users/{id}")
    public User retrieve(@PathVariable Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new UserNotFoundException(id));
    }
}
