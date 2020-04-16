package com.concrete.desafiojava.controller;

import com.concrete.desafiojava.error.ErrorResponse;
import com.concrete.desafiojava.exception.UserNotFoundException;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.service.UserService;
import com.concrete.desafiojava.validator.UserValidator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;


    @GetMapping("/users")
    public ResponseEntity<Object>  list() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object>  create(@RequestBody User newUser) {
        Errors errors = new BeanPropertyBindingResult(newUser, "user");
        userValidator.validate(newUser, errors);

        if (errors.hasErrors()) {
            List<ErrorResponse> errorsResponse = errors.getAllErrors().stream()
                                                                      .map(e -> new ErrorResponse(e.getDefaultMessage()))
                                                                      .collect(Collectors.toList());
            return new ResponseEntity<>(errorsResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userService.create(newUser), HttpStatus.OK);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<Object> retrieve(@PathVariable UUID id) {
        try {
            User user = userService.findById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
