package com.boun.swe.mnemosyne.config;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.enums.Role;
import com.boun.swe.mnemosyne.model.Location;
import com.boun.swe.mnemosyne.model.Memory;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.repository.MemoryRepository;
import com.boun.swe.mnemosyne.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class DataLoader {

    private MemoryRepository memoryRepository;
    private UserService userService;

    @Autowired
    public DataLoader(UserService userService, MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
        this.userService = userService;
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
                .email("patates@example.com")
                .password("123")
                .role(Role.USER)
                .username("patateskafa")
                .build();


        User blue = User.builder()
                .id(3L)
                .email("blue@example.com")
                .password("123")
                .role(Role.USER)
                .username("blue")
                .build();

        userService.save(admin);
        userService.save(user);
        userService.save(blue);

        Memory privateMemory = Memory.builder()
                .id(1L)
                .title("70's Berlin")
                .text("There was a wall and it was high. Then it went down. Am I not the best writer ever?")
                .year(2018)
                .month(11)
                .day(null)
                .duration(1)
                .period("Days")
                .isPublished(true)
                .locations(Collections.singleton(new Location(1L, "Berlin", 52.520008, 13.404954, Collections.emptySet())))
                .type(MemoryType.PUBLIC)
                .user(user)
                .build();

        Memory publicMemory = Memory.builder()
                .id(2L)
                .title("90's Somalia")
                .text("Those were the days in which we could go somewhere without coming across some pirates!\n Why are there everywhere anyway?")
                .year(1990)
                .month(null)
                .day(null)
                .duration(1)
                .period("Decades")
                .isPublished(true)
                .locations(Collections.singleton(new Location(1L, "Berlin", 52.520008, 13.404954, Collections.emptySet())))
                .type(MemoryType.PUBLIC)
                .user(blue)
                .build();

        Memory examMemory = Memory.builder()
                .id(3L)
                .title("Worst exam ever!")
                .text("I will never forget that exam. People started arguing without caring about anyone else. I couldn't focus!")
                .year(2009)
                .month(8)
                .day(3)
                .duration(2)
                .period("Hours")
                .isPublished(true)
                .locations(Collections.singleton(new Location(1L, "Berlin", 52.520008, 13.404954, Collections.emptySet())))
                .type(MemoryType.PUBLIC)
                .user(blue)
                .build();

        memoryRepository.save(privateMemory);
        memoryRepository.save(publicMemory);
        memoryRepository.save(examMemory);
    }
}
