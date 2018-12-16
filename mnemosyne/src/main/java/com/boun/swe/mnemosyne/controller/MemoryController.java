package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.service.MemoryService;
import com.boun.swe.mnemosyne.service.UserService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Validated
@Controller
@RequestMapping
public class MemoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryController.class);

    private final MemoryService memoryService;
    private final UserService userService;

    @Autowired
    public MemoryController(MemoryService memoryService, UserService userService) {
        this.memoryService = memoryService;
        this.userService = userService;
    }

    @PostMapping(value = "/memories/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createMemory(@ModelAttribute("memoryTitle") @NotBlank final String title, final Model model) {
        LOGGER.info("Create memory request received with memory title: {}", title);
        Memory createdMemory = memoryService.createMemory(
                Memory.builder().title(title).type(MemoryType.PRIVATE).build());
        model.addAttribute("memory", createdMemory);
        return "memories";
    }

    @PatchMapping(value = "/memories/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchUpdateMemory(@ModelAttribute("memoryForm") @NotNull final Memory memory, final Model model) {
        LOGGER.info("Create memory request received with memory title: {}", memory.getTitle());
        Memory updatedMemory = memoryService.updateMemory(memory);
        model.addAttribute("updatedMemory", updatedMemory);
        return "memories";
    }

    @GetMapping(value = "/memories", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllPublicMemories(final Model model) {
        LOGGER.info("Get all public memories request received");
        List<Memory> memories = memoryService.getAllMemoriesByType(MemoryType.PUBLIC);
        model.addAttribute("publicMemories", memories);
        return "memories";
    }

    @GetMapping(value = "/memories/{memoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMemoryById(@PathVariable("memoryId") final Long memoryId, final Principal principal, final Model model) {
        LOGGER.info("Get memory request received for memoryId: {}", memoryId);
        User user = userService.findByUsername(principal.getName());
        Memory memory = memoryService.getMemoryById(memoryId);

        if (memory == null) {
            final String errorMsg = "Memory with id: " + memoryId + " not found!";
            LOGGER.warn(errorMsg);
            model.addAttribute("memoryNotFound", errorMsg);
        }

        validateMemoryByUser(model, user, memory);
        return "memories";
    }

    @GetMapping(value = "/user/{userId}/memories", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMemoriesByUser(@PathVariable("userId") final Long userId, @RequestParam("memoryType") final String memoryType,
                                    final Principal principal, final Model model) {
        LOGGER.info("Get memories request received by userId: {} with memoryType: {}", userId, memoryType);
        User user = userService.findByUsername(principal.getName());
        final List<Memory> memories = findRequestedMemories(userId, memoryType, user);
        model.addAttribute("userPublicMemories", memories);
        return "memories";
    }

    @GetMapping(value = "/friendships/memories", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFollowingsMemories(final Principal principal, final Model model) {
        User user = userService.findByUsername(principal.getName());
        LOGGER.info("Get memories of followings request received for user: {}", user.getId());
        Set<Memory> followingsMemories = new HashSet<>();
        user.getFollowingUsers().forEach(followingUser -> {
            followingsMemories.addAll(memoryService.getAllMemoriesByTypeAndUser(MemoryType.PUBLIC, followingUser.getId()));
            followingsMemories.addAll(memoryService.getAllMemoriesByTypeAndUser(MemoryType.SOCIAL, followingUser.getId()));
        });
        model.addAttribute("followingsMemories", followingsMemories);
        return "memories";
    }

    private List<Memory> findRequestedMemories(Long userId, String memoryType, User userInRequest) {
        List<Memory> memories;
        if (userInRequest != null && userInRequest.getId() == userId) {
            switch (memoryType) {
                case "public":
                    memories = memoryService.getAllMemoriesByTypeAndUser(MemoryType.PUBLIC, userId);
                    break;
                case "social":
                    memories = memoryService.getAllMemoriesByTypeAndUser(MemoryType.SOCIAL, userId);
                    break;
                case "private":
                    memories = memoryService.getAllMemoriesByTypeAndUser(MemoryType.PRIVATE, userId);
                    break;
                default:
                    memories = memoryService.getAllMemoriesByUser(userId);
                    break;
            }
        } else {
            memories = memoryService.getAllMemoriesByTypeAndUser(MemoryType.PUBLIC, userId);
        }
        return memories;
    }

    private void validateMemoryByUser(Model model, User user, Memory memory) {

        if (memory.getType().equals(MemoryType.PUBLIC)) {
            model.addAttribute("memory", memory);
            return;
        }

        if (user == null) {
            final String errorMsg =
                    String.format("Memory: %s is not available to unregistered user", memory.getId());
            model.addAttribute("memoryNotAvailable", errorMsg);
            return;
        }

        if (memory.getType().equals(MemoryType.PRIVATE)) {
            if (user.equals(memory.getUser())) {
                model.addAttribute("memory", memory);
                return;
            }
        }

        if (memory.getType().equals(MemoryType.SOCIAL)) {
            if (user.getFollowingUsers().contains(memory.getUser())) {
                model.addAttribute("memory", memory);
                return;
            }
        }

        final String errorMsg = String.format(
                "User: %s has no access to memoryId: %s" + user.getUsername(), user.getId());
        model.addAttribute("memoryNotAvailable", errorMsg);
    }
}
