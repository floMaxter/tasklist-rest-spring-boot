package com.projects.tasklist.web.mappers;

import com.projects.tasklist.domain.task.Task;
import com.projects.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
