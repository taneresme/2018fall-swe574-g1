package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.service.MemoryService;
import com.boun.swe.mnemosyne.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);

    private UserService userService;
    private final MemoryService memoryService;

    @Autowired
    public ViewController(final UserService userService, MemoryService memoryService) {
        this.userService = userService;
        this.memoryService = memoryService;
    }

    @GetMapping(value = "/home")
    public String registration(Principal principal, final Model model) {
        /* add authenticated user principle */
        model.addAttribute("principal", principal);

        User user = userService.findByUsername(principal.getName());
        LOGGER.info("Get memories of followings request received for user: {}", user.getId());
        Set<Memory> followingsMemories = new HashSet<>();
        user.getFollowingUsers().forEach(followingUser -> {
            followingsMemories.addAll(memoryService.getAllMemoriesByTypeAndUser(MemoryType.PUBLIC, followingUser.getId()));
            followingsMemories.addAll(memoryService.getAllMemoriesByTypeAndUser(MemoryType.SOCIAL, followingUser.getId()));
        });
        model.addAttribute("memories", memoryService.getAllMemoriesByType(MemoryType.PUBLIC));
        model.addAttribute("followingsMemories", followingsMemories);

        return "home";
    }

    // TODO: map to /memories
    @GetMapping(value = "/memories/add")
    public String memoryView() {
        return "memories";
    }


    // TODO: this shouldn't be here, and handled by view side?
    @GetMapping(value = "/memories/{memoryId}")
    public String getMemoryById(@PathVariable("memoryId") final Long memoryId, final Principal principal, final Model model) {
        LOGGER.info("Get memory request received for memoryId: {}", memoryId);
        model.addAttribute("principal", principal);
        Memory memory = memoryService.getMemoryById(memoryId);
        model.addAttribute("memory", memory);
        return "memory_view";
    }

}
