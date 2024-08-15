package com.projects.tasklist.web.controller;

import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.service.TaskService;
import com.projects.tasklist.service.UserService;
import com.projects.tasklist.web.dto.task.TaskDto;
import com.projects.tasklist.web.dto.user.UserDto;
import com.projects.tasklist.web.dto.validation.OnCreate;
import com.projects.tasklist.web.dto.validation.OnUpdate;
import com.projects.tasklist.web.mappers.TaskMapper;
import com.projects.tasklist.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    private final TaskService taskService;

    private final UserMapper userMapper;

    private final TaskMapper taskMapper;

    @PutMapping
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        User user = userService.getById(userId);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.delete(userId);
    }

    @GetMapping("/{userId}/tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskService.getAllByUserId(userId);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{userId}/tasks")
    public TaskDto createTask(@PathVariable Long userId,
                              @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(task, userId);
        return taskMapper.toDto(createdTask);
    }
}
