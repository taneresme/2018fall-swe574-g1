package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.request.ChangePasswordRequest;
import com.boun.swe.mnemosyne.request.ProfileUpdateRequest;
import com.boun.swe.mnemosyne.service.MemoryService;
import com.boun.swe.mnemosyne.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    private UserService userService;
    private final MemoryService memoryService;

    @Autowired
    public ProfileController(final UserService userService, MemoryService memoryService) {
        this.userService = userService;
        this.memoryService = memoryService;
    }

    @GetMapping(value = {"/profile", "/profile/{userId}"})
    public String getProfile(Principal principal, final Model model, @PathVariable(value = "userId", required = false) final Long userId) {
        /* add authenticated user principle */
        model.addAttribute("principal", principal);
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("principalUser", user);

        if (userId != null) {
            /* Get user */
            User userById = userService.findByUserId(userId);
            /* If there is no user with the provided Id */
            if (userById == null || userById.getId() == user.getId()) {
            /* We will be routing the user to their own profile */
                return "redirect:/profile";
            }

            Boolean followed = user.getFollowingUsers().contains(userById);

            List<Memory> memories = memoryService.getAllMemoriesByTypeAndUser(MemoryType.PUBLIC, userById.getId());
            model.addAttribute("user", userById);
            model.addAttribute("memories", memories);
            model.addAttribute("followers", userById.getFollowers());
            model.addAttribute("followed", followed);
            model.addAttribute("unfollowed", !followed);
        } else {
            /* Self */
            List<Memory> memories = memoryService.getAllMemoriesByUser(user.getId());
            model.addAttribute("self", true);
            model.addAttribute("user", user);
            model.addAttribute("memories", memories);
            model.addAttribute("followers", user.getFollowers());
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
        if (!((UsernamePasswordAuthenticationToken) principal).getCredentials().equals(request.getOldPassword())) {
            throw new Exception("Passport mismatch!");
            //return ResponseEntity.status(400).body("{'error' : 'Passport mismatch!'}");
        }
        /* Update the current user's parameters */
        User user = userService.findByUsername(principal.getName());
        user.setPassword(request.getNewPassword());
        userService.save(user);

        return ResponseEntity.ok("{}");
    }

    @PostMapping(value = "/profile/{userId}/follow", produces = MediaType.APPLICATION_JSON_VALUE)
    public String followUser(@PathVariable("userId") final Long userId, final Principal principal, final Model model) {
        final User user = userService.findByUsername(principal.getName());

        /* Perform follow-user action */
        boolean isFollowed = userService.followUser(user, userId);
        if (!isFollowed) {
            model.addAttribute("followUserError", "Unable to follow user with userId: " + userId);
            return "redirect:/profile/" + userId;
        }
        model.addAttribute("followUserSuccess", userId);

        return "redirect:/profile/" + userId;
    }

    @PostMapping(value = "/profile/{userId}/unfollow", produces = MediaType.APPLICATION_JSON_VALUE)
    public String unFollowUser(@PathVariable("userId") final Long userId, final Principal principal, final Model model) {
        final User user = userService.findByUsername(principal.getName());

        /* Perform unfollow-user action */
        boolean isUnFollowed = userService.unFollowUser(user, userId);
        if (!isUnFollowed) {
            model.addAttribute("unFollowUserError", "Unable to follow user with userId: " + userId);
            return "redirect:/profile/" + userId;
        }
        model.addAttribute("unFollowUserSuccess", userId);

        return "redirect:/profile/" + userId;
    }
}
