package com.concrete.desafiojava.validator;

import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserRepository repository;

    @Override
    public boolean supports(Class aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Email is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password is empty");

        if (repository.findByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "email.duplicate", "Email already exists");
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}
