package com.projects.tasklist.web.controller;

import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.domain.task.TaskImage;
import com.projects.tasklist.service.TaskService;
import com.projects.tasklist.web.dto.task.TaskDto;
import com.projects.tasklist.web.dto.task.TaskImageDto;
import com.projects.tasklist.web.dto.validation.OnUpdate;
import com.projects.tasklist.web.mappers.TaskImageMapper;
import com.projects.tasklist.web.mappers.TaskMapper;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    private final TaskImageMapper taskImageMapper;

    @PutMapping
    @MutationMapping
    @Operation(summary = "update task")
    @PreAuthorize("canAccessTask(#taskDto.id)")
    public TaskDto updateTask(
            @Validated(OnUpdate.class)
            @RequestBody
            @Argument(name = "dto") final TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{taskId}")
    @QueryMapping(name = "taskById")
    @Operation(summary = "Get task by id")
    @PreAuthorize("canAccessTask(#taskId)")
    public TaskDto getTaskById(
            @PathVariable
            @Argument(name = "id") final Long taskId) {
        Task task = taskService.getById(taskId);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{taskId}")
    @MutationMapping(name = "deleteTask")
    @Operation(summary = "Delete task by id")
    @PreAuthorize("canAccessTask(#taskId)")
    public void deleteTaskById(
            @PathVariable
            @Argument(name = "id") final Long taskId) {
        taskService.delete(taskId);
    }

    @PostMapping("/{taskId}/image")
    @Operation(summary = "Upload image for task")
    @PreAuthorize("canAccessTask(#taskId)")
    public void uploadImage(@PathVariable final Long taskId,
                            @Validated
                            @ModelAttribute final TaskImageDto imageDto) {
        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(taskId, image);
    }
}
