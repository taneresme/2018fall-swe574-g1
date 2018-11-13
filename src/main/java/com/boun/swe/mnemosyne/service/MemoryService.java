package com.boun.swe.mnemosyne.service;

import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.repository.MemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.title;

@Service
public class MemoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryService.class);

    private MemoryRepository memoryRepository;

    @Autowired
    public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    public Memory createMemory(Memory memory) {
        Memory storedMemory = memoryRepository.save(memory);
        LOGGER.info("Memory with title: {} created successfully", title);
        return storedMemory;
    }

    public List<Memory> getAllPublicMemories() {
        LOGGER.info("Retrieving all published public memories");
        return memoryRepository.findByIsPublishedTrueAndIsPublicTrue();
    }

    public Memory updateMemory(Memory memory) {
        Memory storedMemory = memoryRepository.save(memory);
        LOGGER.info("Memory with title: {} updated successfully", title);
        return storedMemory;
    }
}
