package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller(value = "/friendships")
public class SocialController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocialController.class);

    private UserService userService;

    @Autowired
    public SocialController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String followUser(@RequestParam("userId") final Long userId, final Principal principal, final Model model) {
        final User user = userService.findByUsername(principal.getName());
        LOGGER.info("Follow user: {} retrieved for user: {}", userId, user.getId());
        boolean isFollowed = userService.followUser(user, userId);
        if (!isFollowed) {
            model.addAttribute("followUserError", "Unable to follow user with userId: " + userId);
            return "social";
        }
        model.addAttribute("followUserSuccess", userId);
        return "social";
    }

    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public String unFollowUser(@RequestParam("userId") final Long userId, final Principal principal, final Model model) {
        final User user = userService.findByUsername(principal.getName());
        LOGGER.info("Unfollow user: {} retrieved for user: {}", userId, user.getId());
        boolean isUnFollowed = userService.unFollowUser(user, userId);
        if (!isUnFollowed) {
            model.addAttribute("unFollowUserError", "Unable to follow user with userId: " + userId);
            return "social";
        }
        model.addAttribute("unFollowUserSuccess", userId);
        return "social";
    }

    @GetMapping(value = "/followers", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFollowers(final Principal principal, final Model model) {
        final User user = userService.findByUsername(principal.getName());
        LOGGER.info("Get followers of user: {} request retrieved ", user.getId());
        model.addAttribute("followers", user.getFollowers());
        return "social";
    }

    @GetMapping(value = "/followings", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFollowingUsers(final Principal principal, final Model model) {
        final User user = userService.findByUsername(principal.getName());
        LOGGER.info("Get following users of user: {} request retrieved ", user.getId());
        model.addAttribute("followingUsers", user.getFollowingUsers());
        return "social";
    }

    @GetMapping(value = "/{userId}/followers", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFollowersByUser(@PathVariable("userId") final Long userId, final Model model) {
        LOGGER.info("Get followers of user: {} request retrieved ", userId);
        final User user = userService.findByUserId(userId);
        model.addAttribute("followers", user.getFollowers());
        return "social";
    }

    @GetMapping(value = "/{userId}/followings", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFollowingsByUser(@PathVariable("userId") final Long userId, final Model model) {
        LOGGER.info("Get following users of user: {} request retrieved ", userId);
        final User user = userService.findByUserId(userId);
        model.addAttribute("followers", user.getFollowingUsers());
        return "social";
    }

}