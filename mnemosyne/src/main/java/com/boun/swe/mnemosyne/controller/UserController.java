package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.service.SecurityService;
import com.boun.swe.mnemosyne.service.UserService;
import com.boun.swe.mnemosyne.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private SecurityService securityService;
    private UserValidator userValidator;

    @Autowired
    public UserController(final UserService userService, final SecurityService securityService, final UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/register")
    public String registration(final Model model) {
        model.addAttribute("userForm", new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String postRegister(@ModelAttribute("userForm") final User userForm,
                               final BindingResult bindingResult, final Model model) {
        LOGGER.info("Registration request received for user: {}", userForm.getUsername());
        userValidator.validate(userForm, bindingResult);

        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "register";
        }

        userService.save(userForm);

        model.addAttribute("username", userForm.getUsername());
        return "welcome";
    }

    @GetMapping(value = {"/login"})
    public String login(final Model model) {
        return "login";
    }

    @GetMapping(value = "/")
    public String index(final Model model) {
        return "index";
    }

    @PostMapping(value = "/login")
    public String postLogin(@ModelAttribute("form") final User userForm, final Model model) {
        LOGGER.info("Login request received for user: {}", userForm.toString());
        boolean isAuthenticated = securityService
                .authenticate(userForm.getUsername(), userForm.getPassword());

        if (!isAuthenticated) {
            model.addAttribute("loginError", "Username or password incorrect!");
            return "login";
        }

        model.addAttribute("username", userForm.getUsername());
        LOGGER.info("User: {} successfully logged in", userForm.getUsername());
        return "welcome";
    }
}
