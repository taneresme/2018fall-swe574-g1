package com.boun.swe.mnemosyne.config;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.enums.Role;
import com.boun.swe.mnemosyne.model.Location;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.repository.MemoryRepository;
import com.boun.swe.mnemosyne.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;

@Component
public class DataLoader {

    private UserRepository userRepository;
    private MemoryRepository memoryRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, MemoryRepository memoryRepository) {
        this.userRepository = userRepository;
        this.memoryRepository = memoryRepository;
    }

    @PostConstruct
    public void loadData() {
        User admin = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("12345")
                .role(Role.ADMIN)
                .username("admin")
                .build();

        User user = User.builder()
                .id(2L)
                .email("user@example.com")
                .password("12345")
                .role(Role.USER)
                .username("user")
                .build();

        userRepository.save(admin);
        userRepository.save(user);

        Memory privateMemory = Memory.builder()
                .id(1L)
                .title("70's Berlin")
                .text("there was a wall!")
                .dateFrom(new Date(100_000_000))
                .dateTo(new Date(150_000_000))
                .isPublished(true)
                .locations(Collections.singleton(new Location(1L, "Berlin", 52.520008, 13.404954, Collections.emptySet())))
                .type(MemoryType.PUBLIC)
                .user(admin)
                .build();

        Memory publicMemory = Memory.builder()
                .id(2L)
                .title("90's Berlin")
                .text("wall is gone!")
                .dateFrom(new Date(100_000_000))
                .dateTo(new Date(150_000_000))
                .isPublished(true)
                .locations(Collections.singleton(new Location(1L, "Berlin", 52.520008, 13.404954, Collections.emptySet())))
                .type(MemoryType.PUBLIC)
                .user(user)
                .build();

        memoryRepository.save(privateMemory);
        memoryRepository.save(publicMemory);
    }
}
