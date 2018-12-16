package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.exception.MemoryNotFoundException;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.service.MemoryService;
import com.boun.swe.mnemosyne.service.UserService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
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
    public Memory createMemory(@RequestParam("title") @NotBlank final String title, final Principal principal) {
        LOGGER.info("Create memory request received with memory title: {}", title);
        /*User user = userService.findByUsername(principal.getName());
        if (user == null) {
            // TODO: throw exception here
            LOGGER.warn("User cannot create memory");
            return null;
        }*/
        User user = userService.findByUserId(2L);
        return memoryService.createMemory(Memory.builder().title(title).type(MemoryType.PRIVATE).user(user).build());
    }

    @PatchMapping(value = "/memories/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void patchUpdateMemory(@RequestBody @NotNull final Memory memory, final Principal principal) {
        LOGGER.info("Create memory request received with memory title: {}", memory.getTitle());
        // TODO: update with principal
        // User user = userService.findByUsername(principal.getName());
        User user = userService.findByUserId(2L);
        memory.setUser(user);
        memoryService.updateMemory(memory);
    }

    @GetMapping(value = "/memories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Memory> getAllPublicMemories() {
        LOGGER.info("Get all public memories request received");
        return memoryService.getAllMemoriesByType(MemoryType.PUBLIC);
    }

    @GetMapping(value = "/memories/{memoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Memory getMemoryById(@PathVariable("memoryId") final Long memoryId, final Principal principal) {
        LOGGER.info("Get memory request received for memoryId: {}", memoryId);
        // User user = userService.findByUsername(principal.getName());
        Memory memory = memoryService.getMemoryById(memoryId);

        /*if (memory == null) {
            final String errorMsg = "Memory with id: " + memoryId + " not found!";
            LOGGER.warn(errorMsg);
            throw new MemoryNotFoundException(errorMsg);
        }

        return validateMemoryByUser(user, memory);*/
        return memory;
    }

    @GetMapping(value = "/user/{userId}/memories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Memory> getMemoriesByUser(@PathVariable("userId") final Long userId, @RequestParam("memoryType") final String memoryType,
                                          final Principal principal) {
        LOGGER.info("Get memories request received by userId: {} with memoryType: {}", userId, memoryType);
        User user = userService.findByUsername(principal.getName());
        return findRequestedMemories(userId, memoryType, user);
    }

    @GetMapping(value = "/friendships/memories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Memory> getFollowingsMemories(final Principal principal) {
        User user = userService.findByUsername(principal.getName());
        LOGGER.info("Get memories of followings request received for user: {}", user.getId());
        final List<Memory> followingsMemories = new ArrayList<>();
        user.getFollowingUsers().forEach(followingUser -> {
            followingsMemories.addAll(memoryService.getAllMemoriesByTypeAndUser(MemoryType.PUBLIC, followingUser.getId()));
            followingsMemories.addAll(memoryService.getAllMemoriesByTypeAndUser(MemoryType.SOCIAL, followingUser.getId()));
        });
        return followingsMemories;
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

    private Memory validateMemoryByUser(User user, Memory memory) {

        if (memory.getType().equals(MemoryType.PUBLIC)) {
            return memory;
        }

        if (user == null) {
            final String errorMsg =
                    String.format("Memory: %s is not available to unregistered user", memory.getId());
            throw new MemoryNotFoundException(errorMsg);
        }

        if (memory.getType().equals(MemoryType.PRIVATE)) {
            if (user.equals(memory.getUser())) {
                return memory;
            }
        }

        if (memory.getType().equals(MemoryType.SOCIAL)) {
            if (user.getFollowingUsers().contains(memory.getUser())) {
                return memory;
            }
        }

        final String errorMsg = String.format(
                "User: %s has no access to memoryId: %s" + user.getUsername(), user.getId());
        throw new MemoryNotFoundException(errorMsg);
    }
}
