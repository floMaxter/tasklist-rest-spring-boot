package com.projects.tasklist.service.impl;

import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import com.projects.tasklist.domain.task.Status;
import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.domain.task.TaskImage;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.repository.TaskRepository;
import com.projects.tasklist.service.ImageService;
import com.projects.tasklist.service.TaskService;
import com.projects.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#taskId")
    public Task getById(final Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(final Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(final Task task) {
        Task existing = taskRepository.getReferenceById(task.getId());
        if (task.getStatus() == null) {
            existing.setStatus(Status.TODO);
        } else {
            existing.setStatus(task.getStatus());
        }
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setExpirationDate(task.getExpirationDate());
        taskRepository.save(task);
        return task;
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", key = "#task.id")
    public Task create(final Task task, final Long userId) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        User user = userService.getById(userId);
        user.getTasks().add(task);
        userService.update(user);
        taskRepository.save(task);
        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#taskId")
    public void delete(final Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#taskId")
    public void uploadImage(final Long taskId, final TaskImage image) {
        Task task = getById(taskId);
        String fileName = imageService.upload(image);
        task.getImages().add(fileName);
        taskRepository.save(task);
    }
}
