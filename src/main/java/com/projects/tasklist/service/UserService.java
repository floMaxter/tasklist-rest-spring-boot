package com.projects.tasklist.service;

import com.projects.tasklist.domain.user.User;

public interface UserService {

    User getById(Long userId);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    boolean isTaskOwner(Long userId, Long taskId);

    User getTaskAuthor(Long taskId);

    void delete(Long userId);

}
