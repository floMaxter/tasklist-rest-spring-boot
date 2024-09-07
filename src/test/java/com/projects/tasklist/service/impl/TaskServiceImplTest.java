package com.projects.tasklist.service.impl;

import com.projects.tasklist.config.TestConfig;
import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import com.projects.tasklist.domain.task.Status;
import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.domain.task.TaskImage;
import com.projects.tasklist.repository.TaskRepository;
import com.projects.tasklist.service.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getByIdTaskExistsReturnsNotEmptyOptional() {
        // given
        var id = 1L;
        var task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        // when
        var testTask = taskService.getById(id);

        // then
        Mockito.verify(taskRepository).findById(id);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void getByIdTaskDoesNotExistThrowsResourceNotFoundException() {
        // given
        var id = 1L;
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        // when

        // then
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
        Mockito.verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void getAllByUserIdReturnsTaskList() {
        // given
        var id = 1L;
        var tasks = IntStream.range(1, 6)
                .mapToObj(i -> new Task()).toList();

        Mockito.when(taskRepository.findAllByUserId(id))
                .thenReturn(tasks);

        // when
        var testTasks = taskService.getAllByUserId(id);

        // then
        Mockito.verify(taskRepository).findAllByUserId(id);
        Assertions.assertEquals(tasks, testTasks);
    }

    @Test
    void updateStatusIsEmptyReturnsNotEmptyOptional() {
        // given
        var id = 1L;
        var task = new Task();
        task.setId(id);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setExpirationDate(LocalDateTime.now());

        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        // when
        var testTask = taskService.update(task);

        // then
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(Status.TODO, testTask.getStatus());
    }

    @Test
    void updateStatusIsNotEmptyReturnsNotEmptyOptional() {
        // given
        var id = 1L;
        var task = new Task();
        task.setId(id);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setStatus(Status.IN_PROGRESS);
        task.setExpirationDate(LocalDateTime.now());

        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        // when
        var testTask = taskService.update(task);

        // then
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void createStatusIsEmpty() {
        // given
        var taskId = 1L;
        var userId = 1L;
        var task = new Task();

        Mockito.doAnswer(invocation -> {
                    Task savedTask = invocation.getArgument(0);
                    savedTask.setId(taskId);
                    return savedTask;
                })
                .when(taskRepository).save(task);

        // when
        var testTask = taskService.create(task, userId);

        // then
        Mockito.verify(taskRepository).save(task);
        Mockito.verify(taskRepository).assignTask(userId, task.getId());
        Assertions.assertNotNull(testTask.getId());
    }

    @Test
    void createStatusIsNotEmpty() {
        // given
        var taskId = 1L;
        var userId = 1L;
        var task = new Task();
        task.setStatus(Status.IN_PROGRESS);

        Mockito.doAnswer(invocation -> {
                    Task savedTask = invocation.getArgument(0);
                    savedTask.setId(taskId);
                    return savedTask;
                })
                .when(taskRepository).save(task);

        // when
        var testTask = taskService.create(task, userId);

        // then
        Mockito.verify(taskRepository).save(task);
        Mockito.verify(taskRepository).assignTask(userId, task.getId());
        Assertions.assertEquals(Status.IN_PROGRESS, testTask.getStatus());
        Assertions.assertNotNull(testTask.getId());
    }

    @Test
    void deleteDeletesTask() {
        // given
        var id = 1L;

        // when
        taskService.delete(id);

        // then
        Mockito.verify(taskRepository).deleteById(id);
        Mockito.verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void uploadImageUploadsImage() {
        // given
        var id = 1L;
        String imageName = "imageName";
        TaskImage taskImage = new TaskImage();
        Mockito.when(imageService.upload(taskImage))
                .thenReturn(imageName);

        // when
        taskService.uploadImage(id, taskImage);

        // then
        Mockito.verify(taskRepository).addImage(id, imageName);
    }
}