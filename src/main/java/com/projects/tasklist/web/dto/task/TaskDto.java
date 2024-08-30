package com.projects.tasklist.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.projects.tasklist.domain.task.Status;
import com.projects.tasklist.web.dto.validation.OnCreate;
import com.projects.tasklist.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Task DTO")
public class TaskDto {

    @Schema(description = "Task id", example = "1")
    @NotNull(message = "Id shouldn't be null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Task title", example = "Do homework")
    @NotNull(message = "Title shouldn't be null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Title should be less than 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Schema(description = "English workbook ex 12 p 45")
    @Length(max = 255,
            message = "Description should be less than 255 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Schema(description = "Task status", example = "TODO")
    private Status status;

    @Schema(description = "Task expiration date",
            example = "2024-08-29 12:00:00.000000")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;
}
