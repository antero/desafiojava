package com.concrete.desafiojava.validator;

import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.findByUsername(user.getEmail()) != null) {
            errors.rejectValue("email", "Email ja existente");
        }
    }
}
