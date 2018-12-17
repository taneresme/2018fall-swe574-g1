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

import java.security.Principal;

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
    public String registration(Principal principal, final Model model) {
        /* If the user is already authenticated */
        if (principal != null) {
            return "redirect:/home";
        }

        /* Add empty user */
        model.addAttribute("userForm", new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String postRegister(@ModelAttribute("userForm") final User userForm,
                               final BindingResult bindingResult, final Model model) {
        LOGGER.info("Registration request received for user: {}", userForm.getUsername());
        userValidator.validate(userForm, bindingResult);

        /*Adding userForm again to the model to populate on the screen in the case of error
        * to prevent the user fill the blanks again */
        model.addAttribute("userForm", userForm);
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "register";
        }

        userService.save(userForm);

        /* Authenticate the user after successful registration */
        securityService.authenticate(userForm.getUsername(), userForm.getPassword());

        return "redirect:/home";
    }

    @GetMapping(value = "/login")
    public String getLogin(Principal principal, final Model model) {
        /* If the user is already authenticated */
        if (principal != null) {
            return "redirect:/home";
        }

        /* Add empty user */
        model.addAttribute("form", new User());
        return "login";

    }

    @GetMapping(value = "/")
    public String index(Principal principal, final Model model) {
        /* add authenticated user principle */
        model.addAttribute("principal", principal);

        return "index";
    }

    /* I changed the POST address of the login form to eliminate the Spring login action */
    @PostMapping(value = "/signin")
    public String postLogin(@ModelAttribute("form") final User userForm, final Model model) {
        LOGGER.info("Login request received for user: {}", userForm.toString());
        boolean isAuthenticated = securityService
                .authenticate(userForm.getUsername(), userForm.getPassword());

        if (!isAuthenticated) {
            /*Adding userForm again to the model to populate on the screen in the case of error
            ** to prevent the user fill the blanks again */
            model.addAttribute("form", userForm);
            model.addAttribute("loginError", "Username or password incorrect!");
            return "login";
        }

        LOGGER.info("User: {} successfully logged in", userForm.getUsername());
        return "redirect:/home";
    }
}
