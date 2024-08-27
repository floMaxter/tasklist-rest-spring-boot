package com.projects.tasklist.service;

import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.domain.task.TaskImage;

import java.util.List;

public interface TaskService {

    Task getById(Long taskId);

    List<Task> getAllByUserId(Long userId);

    Task update(Task task);

    Task create(Task task, Long userId);

    void delete(Long taskId);

    void uploadImage(Long taskId, TaskImage image);
}
