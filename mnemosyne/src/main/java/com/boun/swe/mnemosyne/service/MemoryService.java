package com.boun.swe.mnemosyne.service;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.exception.MemoryNotFoundException;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.repository.MemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryService.class);

    private MemoryRepository memoryRepository;

    @Autowired
    public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    public Memory createMemory(Memory memory) {
        final Memory storedMemory = memoryRepository.save(memory);
        LOGGER.info("Memory with title: {} created successfully", memory.getTitle());
        return storedMemory;
    }

    public Memory updateMemory(Memory memory) {
        if (!memoryRepository.exists(memory.getId())) {
            LOGGER.warn("Unable to update memory with id: {} and title: {}",
                    memory.getId(), memory.getTitle());
            throw new MemoryNotFoundException("Unable to find memory with id: " + memory.getId());
        }
        final Memory storedMemory = memoryRepository.save(memory);
        LOGGER.info("Memory with title: {} updated successfully", memory.getTitle());
        return storedMemory;
    }

    public List<Memory> getAllMemoriesByType(final MemoryType memoryType) {
        LOGGER.info("Retrieving all published memories by memoryType: {}", memoryType.name());
        return memoryRepository.findByTypeAndIsPublishedTrue(memoryType);
    }

    public List<Memory> getAllMemoriesByUser(final Long userId) {
        LOGGER.info("Retrieving all published memories by memoryType: {}");
        return memoryRepository.findAllMemoriesByUserId(userId);
    }

    public List<Memory> getAllMemoriesByTypeAndUser(final MemoryType memoryType, final Long userId) {
        LOGGER.info("Retrieving all published memories by memoryType: {} for userId: {}", memoryType.name(), userId);
        return memoryRepository.findByTypeAndIsPublishedTrue(memoryType);
    }

    public List<Memory> getAllMemories() {
        LOGGER.info("Retrieving all published memories");
        return memoryRepository.findAll();
    }

    public Memory getMemoryById(final Long memoryId) {
        LOGGER.info("Retrieving memory by id: {}", memoryId);
        return memoryRepository.findOne(memoryId);
    }

    public boolean isExistingMemory(final Long memoryId) {
        LOGGER.info("Validating memory by id: {}", memoryId);
        return memoryRepository.exists(memoryId);
    }
}
