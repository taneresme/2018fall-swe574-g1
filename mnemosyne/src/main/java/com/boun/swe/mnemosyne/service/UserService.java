package com.boun.swe.mnemosyne.service;

import com.boun.swe.mnemosyne.enums.Role;
import com.boun.swe.mnemosyne.model.User;
import com.boun.swe.mnemosyne.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(final User user) {
        LOGGER.info("Storing user with username: {} and email: {}", user.getUsername(), user.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        LOGGER.info("Successfully stored user with id: {} and username: {}", user.getId(), user.getUsername());
    }

    public User findByUsername(final String username) {
        LOGGER.info("Retrieving user with username: {} ", username);
        return userRepository.findByUsername(username);
    }

    public User findByUserId(final Long userId) {
        LOGGER.info("Retrieving user with userId: {} ", userId);
        return userRepository.findOne(userId);
    }

    public boolean existingUser(final Long userId) {
        LOGGER.info("Checking if the user with userId: {} exists", userId);
        return userRepository.exists(userId);
    }

    public boolean followUser(final User user, final Long userId) {
        if (!existingUser(userId)) {
            LOGGER.warn("User with id: {} does not exist!", userId);
            return false;
        }
        final User userToFollow = findByUserId(userId);
        user.getFollowingUsers().add(userToFollow);
        userToFollow.getFollowers().add(user);
        userRepository.save(user);
        userRepository.save(userToFollow);
        LOGGER.info("Successfully followed user with id: {}", userId);
        return true;
    }

    public boolean unFollowUser(final User user, final Long userId) {
        if (!existingUser(userId)) {
            LOGGER.warn("User with id: {} does not exist!", userId);
            return false;
        }
        final User userToUnFollow = findByUserId(userId);
        user.getFollowingUsers().remove(userToUnFollow);
        userToUnFollow.getFollowers().remove(user);
        userRepository.save(user);
        userRepository.save(userToUnFollow);
        LOGGER.info("Successfully un-followed user with id: {}", userId);
        return true;
    }

    public List<User> getAllUsers() {
        LOGGER.info("Retrieving all users");
        return userRepository.findAll();
    }
}
