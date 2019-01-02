package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.model.Location;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.service.MemoryService;
import com.boun.swe.mnemosyne.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);

    private final UserService userService;
    private final MemoryService memoryService;

    @Autowired
    public ViewController(final UserService userService, MemoryService memoryService) {
        this.userService = userService;
        this.memoryService = memoryService;
    }

    @GetMapping(value = "/")
    public String index(Principal principal, final Model model) {

        List<Memory> memories = memoryService.getAllMemories();
        if (memories.size()> 9){
            memories = memories.subList(0,9);
        }

        List<Location> mapLocations = new ArrayList<>();
        List<Long> memoryIds = new ArrayList<>();
        List<String> memoryTitles = new ArrayList<>();
        StringBuilder sb = new StringBuilder("[");

        for(int i = 0; i < memories.size(); i++){
            mapLocations.add(memories.get(0).getLocations().iterator().next());
            memoryIds.add(memories.get(i).getId());
            memoryTitles.add(memories.get(i).getTitle());
        }

        for(int i = 0; i < mapLocations.size(); i++){
            sb.append(mapLocations.get(i).toJsonString());
            sb.append("\"memoryId\":");
            sb.append("\"").append(memoryIds.get(i)).append("\"");
            sb.append(",");
            sb.append("\"memoryTitle\":");
            sb.append("\"").append(memoryTitles.get(i)).append("\"");
            sb.append("}");
            if(i + 1 < mapLocations.size()) {
                sb.append(",");
            }
        }
        sb.append("]");

        LOGGER.info("Get memory request for all memories");

        /* add authenticated user principle */
        model.addAttribute("principal", principal);
        model.addAttribute("totalMemories", memories.size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("mapLocations", sb.toString());

        return "index";
    }

    @GetMapping(value = "/home")
    public String home(Principal principal, final Model model) {
        /* add authenticated user principle */
        model.addAttribute("principal", principal);
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("principalUser", user);

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

    @GetMapping(value = "/followings")
    public String followings(Principal principal, final Model model) {
        /* add authenticated user principle */
        model.addAttribute("principal", principal);
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("principalUser", user);

        model.addAttribute("followingUsers", user.getFollowingUsers());
        return "followings";
    }

}
