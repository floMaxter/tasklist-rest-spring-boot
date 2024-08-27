package com.projects.tasklist.web.mappers;

import com.projects.tasklist.domain.task.TaskImage;
import com.projects.tasklist.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}
