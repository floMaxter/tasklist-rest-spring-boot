package com.projects.tasklist.service.impl;

import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import com.projects.tasklist.domain.user.Role;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.repository.UserRepository;
import com.projects.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#userId")
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.id"),
            @CachePut(value = "UserService::getByUsername", key = "#user.username")
    })
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        return user;
    }

    @Override
    @Transactional
    @Caching(cacheable = {
            @Cacheable(value = "UserService::getById", key = "#user.id"),
            @Cacheable(value = "UserService::getByUsername", key = "#user.username")
    })
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation don't match.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.create(user);
        userRepository.insertUserRole(user.getId(), Role.ROLE_USER);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::isTaskOwner", key = "#userId" + "." + "#taskId")
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getById", key = "#userId")
    public void delete(Long userId) {
        userRepository.delete(userId);
    }
}
