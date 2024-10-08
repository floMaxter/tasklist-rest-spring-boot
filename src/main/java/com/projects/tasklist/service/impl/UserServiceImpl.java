package com.projects.tasklist.service.impl;

import com.projects.tasklist.domain.MailType;
import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import com.projects.tasklist.domain.user.Role;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.repository.UserRepository;
import com.projects.tasklist.service.MailService;
import com.projects.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    @Override
    @Cacheable(value = "UserService::getById", key = "#userId")
    public User getById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.id"),
            @CachePut(value = "UserService::getByUsername",
                    key = "#user.username")
    })
    public User update(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    @Caching(cacheable = {
            @Cacheable(value = "UserService::getById",
                    condition = "#user.id!=null",
                    key = "#user.id"),
            @Cacheable(value = "UserService::getByUsername",
                    condition = "#user.username!=null",
                    key = "#user.username")
    })
    public User create(final User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException(
                    "Password and password confirmation don't match.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
        return user;
    }

    @Override
    @Cacheable(value = "UserService::isTaskOwner",
            key = "#userId" + "." + "#taskId")
    public boolean isTaskOwner(final Long userId, final Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Cacheable(value = "UserService::getTaskAuthor",
            key = "#taskId")
    public User getTaskAuthor(final Long taskId) {
        return userRepository.findTaskAuthor(taskId).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getById", key = "#userId")
    public void delete(final Long userId) {
        userRepository.deleteById(userId);
    }
}
