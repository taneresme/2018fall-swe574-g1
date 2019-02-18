package com.boun.swe.mnemosyne.service;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.exception.MemoryNotFoundException;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
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
    private List<Memory> last5Memories;

    @Autowired
    public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    private List<Memory> trimUnnecessaryTags(List<Memory> memories){
        for (Memory memory:memories) {
            if (memory.getText() == null) continue;
            /* remove <img> tags from the text */
            memory.setText(memory.getText().replaceAll("<img[^>]*>" ,""));
            /* Find first closing p tag */
            int first = memory.getText().indexOf("</p>");
            if (first > -1) {
                /* Find second closing p tag */
                int second = memory.getText().indexOf("</p>", first);
                if (second > - 1){
                    /* If there is p tag more than 2, we get only 2 of them to display */
                    memory.setText(memory.getText().substring(0, second + 4));
                }
            }
            /* If there is no p tag or more than one, we will display it as is. */
        }
        return memories;
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
        return trimUnnecessaryTags(memoryRepository.findByTypeAndIsPublishedTrue(memoryType));
    }

    public List<Memory> getAllMemoriesByUser(final Long userId) {
        LOGGER.info("Retrieving all published memories by memoryType: {}");
        return trimUnnecessaryTags(memoryRepository.findAllMemoriesByUserId(userId));
    }

    public List<Memory> getAllMemoriesByTypeAndUser(final MemoryType memoryType, final Long userId) {
        LOGGER.info("Retrieving all published memories by memoryType: {} for userId: {}", memoryType.name(), userId);
        return trimUnnecessaryTags(memoryRepository.findByTypeAndIsPublishedTrue(memoryType));
    }

    public List<Memory> getAllMemories() {
        LOGGER.info("Retrieving all published memories");
        return trimUnnecessaryTags(memoryRepository.findAll());
    }

    public Memory getMemoryById(final Long memoryId) {
        LOGGER.info("Retrieving memory by id: {}", memoryId);
        return memoryRepository.findOne(memoryId);
    }

    public boolean isExistingMemory(final Long memoryId) {
        LOGGER.info("Validating memory by id: {}", memoryId);
        return memoryRepository.exists(memoryId);
    }

    public List<Memory> getLast10Memories() {
        LOGGER.info("Retrieving last 5 memories");
        return trimUnnecessaryTags(memoryRepository.findTop10ByTypeAndIsPublishedTrueOrderByIdDesc(MemoryType.PUBLIC));
    }

    public Memory likeMemory(final Memory memory, final User user) {
        LOGGER.info("Like memory : {}", memory);
        memory.getUsersLiked().add(user);
        return memoryRepository.save(memory);
    }

    public Memory unlikeMemory(final Memory memory, final User user) {
        LOGGER.info("UnLike memory : {}", memory);
        memory.getUsersLiked().remove(user);
        return memoryRepository.save(memory);
    }
}
