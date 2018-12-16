package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.request.ChangePasswordRequest;
import com.boun.swe.mnemosyne.request.ProfileUpdateRequest;
import com.boun.swe.mnemosyne.service.SecurityService;
import com.boun.swe.mnemosyne.service.UserService;
import com.boun.swe.mnemosyne.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    private UserService userService;
    private SecurityService securityService;
    private UserValidator userValidator;

    @Autowired
    public ProfileController(final UserService userService, final SecurityService securityService, final UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/profile")
    public String getProfile(Principal principal, final Model model, @RequestParam(value = "userId", required = false) final Long userId) {
        /* add authenticated user principal */
        model.addAttribute("principal", principal);

        if (userId != null) {
            /* Get user */
            User user = userService.findByUserId(userId);
            /* If there is no user with the provided Id */
            if (user == null) {
            /* We will be routing the user to their own profile */
                return "redirect:/profile";
            }
            model.addAttribute("user", user);
        }
        else {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }
        return "profile";
    }

    /* Updates user's profile information */
    @PostMapping(value = "/profile/update")
    @ResponseBody
    public ResponseEntity<String> updateProfileInfo(Principal principal, @RequestBody ProfileUpdateRequest request) {
        /* Update the current user's parameters */
        User user = userService.findByUsername(principal.getName());
        user.setEmail(request.getEmail());
        userService.save(user);

        return ResponseEntity.ok("{}");
    }

    /* Update the existing password of the user */
    @PostMapping(value = "/profile/changepassword")
    @ResponseBody
    public ResponseEntity<String> changePassword(Principal principal, @RequestBody ChangePasswordRequest request) throws Exception {
        /* Check old password */
        if (!((UsernamePasswordAuthenticationToken) principal).getCredentials().equals(request.getOldPassword())){
            throw new Exception("Passport mismatch!");
            //return ResponseEntity.status(400).body("{'error' : 'Passport mismatch!'}");
        }
        /* Update the current user's parameters */
        User user = userService.findByUsername(principal.getName());
        user.setPassword(request.getNewPassword());
        userService.save(user);

        return ResponseEntity.ok("{}");
    }
}
