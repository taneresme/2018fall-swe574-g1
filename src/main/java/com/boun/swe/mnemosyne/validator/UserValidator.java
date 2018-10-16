package com.boun.swe.mnemosyne.validator;

import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidator.class);

    private UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        LOGGER.info("Validating user {}", user);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 4 || user.getUsername().length() > 16) {
            LOGGER.warn("Username {} is not valid!", user.getUsername());
            errors.rejectValue("username", "size.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        User userInDatabase = userService.findByUsername(user.getUsername());

        if (userInDatabase != null) {
            LOGGER.warn("Username {} already exists!", user.getUsername());
            errors.rejectValue("username", "duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 4 || user.getPassword().length() > 16) {
            LOGGER.warn("Password is not valid!");
            errors.rejectValue("password", "size.userForm.password");
        }
    }
}
