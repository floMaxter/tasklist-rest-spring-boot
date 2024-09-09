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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;

    private final TaskService taskService;

    private final UserMapper userMapper;

    private final TaskMapper taskMapper;

    @PutMapping
    @MutationMapping
    @Operation(summary = "Update user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userDto.id)")
    public UserDto updateUser(
            @Validated(OnUpdate.class)
            @RequestBody
            @Argument(name = "dto") final UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{userId}")
    @QueryMapping(name = "userById")
    @Operation(summary = "Get UserDto by id")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public UserDto getUserById(
            @PathVariable
            @Argument(name = "id") final Long userId) {
        User user = userService.getById(userId);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{userId}")
    @MutationMapping(name = "deleteUser")
    @Operation(summary = "Delete user by id")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public void deleteUserById(@PathVariable
                               @Argument(name = "id") final Long userId) {
        userService.delete(userId);
    }

    @GetMapping("/{userId}/tasks")
    @QueryMapping(name = "tasksByUserId")
    @Operation(summary = "Get all tasks by userId")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public List<TaskDto> getTasksByUserId(
            @PathVariable
            @Argument(name = "id") final Long userId) {
        List<Task> tasks = taskService.getAllByUserId(userId);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{userId}/tasks")
    @MutationMapping(name = "createTask")
    @Operation(summary = "Add task to user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public TaskDto createTask(@PathVariable
                              @Argument(name = "id") final Long userId,
                              @Validated(OnCreate.class)
                              @RequestBody
                              @Argument(name = "dto") final TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(task, userId);
        return taskMapper.toDto(createdTask);
    }
}
