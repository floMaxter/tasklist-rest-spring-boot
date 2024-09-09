package com.projects.tasklist.service.impl;

import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import com.projects.tasklist.domain.task.Status;
import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.domain.task.TaskImage;
import com.projects.tasklist.repository.TaskRepository;
import com.projects.tasklist.service.ImageService;
import com.projects.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ImageService imageService;

    @Override
    @Cacheable(value = "TaskService::getById", key = "#taskId")
    public Task getById(final Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Task not found"));
    }

    @Override
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
    @Cacheable(value = "TaskService::getById",
            condition = "#task.id!=null",
            key = "#task.id")
    public Task create(final Task task, final Long userId) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
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
