package com.boun.swe.mnemosyne.service;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.exception.MemoryNotFoundException;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.repository.MemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

    public List<Memory> getAllPublicMemories() {
        LOGGER.info("Retrieving all published public memories");
        return memoryRepository.findByTypeAndIsPublishedTrue(MemoryType.PUBLIC);
    }

    public Memory updateMemory(Memory memory) {
        if (!memoryRepository.exists(Example.of(memory))) {
            LOGGER.warn("Unable to update memory with id: {} and title: {}",
                    memory.getId(), memory.getTitle());
            throw new MemoryNotFoundException("Unable to find memory with id: " + memory.getId());
        }
        final Memory storedMemory = memoryRepository.save(memory);
        LOGGER.info("Memory with title: {} updated successfully", memory.getTitle());
        return storedMemory;
    }

    public List<Memory> getAllPublicMemoriesByUser(Long userId) {
        List<Memory> memories = memoryRepository.findAllMemoriesByTypeAndUserId(MemoryType.PUBLIC, userId);
        LOGGER.info("Memories with userId: {} retrieved successfully", userId);
        return memories;
    }
}
