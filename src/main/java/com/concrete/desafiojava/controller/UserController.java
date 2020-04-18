package com.concrete.desafiojava.controller;

import com.concrete.desafiojava.exception.EmptyTokenException;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.model.UserLoginDetails;
import com.concrete.desafiojava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/usuarios")
    public ResponseEntity<Object>  list() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object>  create(@Valid @RequestBody User newUser) {
        return ResponseEntity.ok(userService.create(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDetails loginDetails) {
        return ResponseEntity.ok(userService.login(loginDetails));
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<Object> profile(@PathVariable UUID id, @RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("token")) {
            throw new EmptyTokenException();
        }
        User user = userService.profile(id, (String) payload.get("token"));
        return ResponseEntity.ok(user);
    }
}
