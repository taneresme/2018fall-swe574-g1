package com.boun.swe.mnemosyne.controller;

import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.service.MemoryService;
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

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Controller
@RequestMapping("/memories")
public class MemoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryController.class);

    private MemoryService memoryService;

    @Autowired
    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createMemory(@ModelAttribute("memoryTitle") @NotBlank final String title, final Model model) {
        LOGGER.info("Create memory request received with memory title: {}", title);
        Memory createdMemory = memoryService.createMemory(Memory.builder().title(title).build());
        model.addAttribute("memory", createdMemory);
        return "memories";
    }

    @PatchMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String patchUpdateMemory(@ModelAttribute("memoryForm") @NotNull final Memory memory, final Model model) {
        LOGGER.info("Create memory request received with memory title: {}", memory.getTitle());
        Memory updatedMemory = memoryService.updateMemory(memory);
        model.addAttribute("updatedMemory", updatedMemory);
        return "memories";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllPublicMemories(final Model model) {
        LOGGER.info("Get all public memories request received");
        List<Memory> memories = memoryService.getAllPublicMemories();
        model.addAttribute("publicMemories", memories);
        return "memories";
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllPublicMemoriesByUser(@PathVariable("userId") final Long userId, final Model model) {
        LOGGER.info("Get all public memories request received");
        List<Memory> memories = memoryService.getAllPublicMemoriesByUser(userId);
        model.addAttribute("userPublicMemories", memories);
        return "memories";
    }
}
